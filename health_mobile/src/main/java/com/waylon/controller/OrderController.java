package com.waylon.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.waylon.constant.MessageConstant;
import com.waylon.entity.Result;
import com.waylon.pojo.Order;
import com.waylon.service.OrderService;
import com.waylon.utils.PhoneFormatCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String mobile) {
        //判断手机号格式
        if (!PhoneFormatCheckUtils.isPhoneLegal(mobile)) {
            return new Result(false, "手机号格式不正确");
        }
        try {
            orderService.createSmsCode(mobile);
            return new Result(true, "验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "验证码发送失败");
        }
    }

    /**
     * 提交预约请求
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //手机号
        String telephone = (String) map.get("telephone");
        //用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码，key为手机号
        String codeInRedis = (String) redisTemplate.boundHashOps("code").get(telephone);

        //校验手机验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Result result = null;
        //调用体检预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()) {
            //预约成功，发送短信通知 todo
            String orderDate = (String) map.get("orderDate");
            orderService.successfulBook(telephone, orderDate);
        }
        return result;
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            //查询预约信息成功
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}