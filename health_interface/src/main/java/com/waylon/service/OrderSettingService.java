package com.waylon.service;

import com.waylon.pojo.OrderSetting;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    public void add(List<OrderSetting> data);

    public List<Map> getOrderSettingByMonth(String date) throws ParseException;

    public void editNumberByDate(OrderSetting orderSetting);

}