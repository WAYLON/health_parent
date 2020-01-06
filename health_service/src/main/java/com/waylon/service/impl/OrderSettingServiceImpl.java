package com.waylon.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.waylon.dao.OrderSettingDao;
import com.waylon.pojo.OrderSetting;
import com.waylon.service.OrderSettingService;
import javafx.scene.input.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务
 */
@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约设置数据
     *
     * @param list
     */
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    //已经进行了预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //没有进行预约设置，执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 根据日期查询预约设置数据
     *
     * @param date
     * @return
     * @throws ParseException
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        Date dateBegin1 = sdf.parse(dateBegin);
        Date dateEnd1 = sdf.parse(dateEnd);
        Map<String, Date> map = new HashMap();
        map.put("dateBegin", dateBegin1);
        map.put("dateEnd", dateEnd1);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Integer> orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            //获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());
            //可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());
            //已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }

    /**
     * 根据日期修改可预约人数
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count =
                orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
}