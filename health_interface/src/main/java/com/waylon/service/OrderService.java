package com.waylon.service;

import com.waylon.entity.Result;

import java.util.Map;

public interface OrderService {

    public void createSmsCode(String mobile);

    public Result order(Map map) throws Exception;

    public Map findById(Integer id) throws Exception;
}
