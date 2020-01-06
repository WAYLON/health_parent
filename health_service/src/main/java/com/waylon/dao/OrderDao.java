package com.waylon.dao;

import com.waylon.pojo.Order;

import java.util.List;

public interface OrderDao {

    public void add(Order order);

    public List<Order> findByCondition(Order order);
}