package com.waylon.service;

import com.waylon.entity.PageResult;
import com.waylon.entity.QueryPageBean;
import com.waylon.pojo.Setmeal;

import java.util.List;

public interface SetmealService {

    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public Setmeal findById(Integer id);

    public List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    public void edit(Setmeal setmeal, Integer[] checkgroupIds);

    public List<Setmeal> findAll();

}
