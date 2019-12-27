package com.waylon.dao;

import com.waylon.pojo.Setmeal;

import java.util.Map;

public interface SetmealDao {

    public void add(Setmeal setmeal);

    public void setSetmealAndCheckGroup(Map<String, Integer> map);
}
