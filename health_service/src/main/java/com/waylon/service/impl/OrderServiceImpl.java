package com.waylon.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.waylon.constant.MessageConstant;
import com.waylon.dao.MemberDao;
import com.waylon.dao.OrderDao;
import com.waylon.dao.OrderSettingDao;
import com.waylon.entity.Result;
import com.waylon.pojo.Member;
import com.waylon.pojo.Order;
import com.waylon.pojo.OrderSetting;
import com.waylon.service.OrderService;
import com.waylon.utils.DateUtils;
import com.waylon.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsDestination;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public void createSmsCode(String mobile) {
        //生成6位随机数
        String code = ValidateCodeUtils.generateValidateCode(6);
        System.out.println("验证码：" + code);
        //存入缓存
        redisTemplate.boundHashOps("code").put(mobile, code);
        //设置过期时间 TODO
        redisTemplate.boundHashOps("code").expire(5L, TimeUnit.MINUTES);
        //发送到activeMQ	....
        jmsTemplate.send(smsDestination, session -> {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setObject("mobile", mobile);//手机号
            mapMessage.setObject("params", code);//参数
            return mapMessage;
        });
    }

    @Override
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            //预约已满，不能预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //防止重复预约
        if (member != null) {
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderDate(date);
            order.setSetmealId(setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //已经完成了预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        if (member == null) {
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        order.setOrderType((String) map.get("orderType"));
        orderDao.add(order);

        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 根据id查询预约信息，包括体检人信息、套餐信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }


}
