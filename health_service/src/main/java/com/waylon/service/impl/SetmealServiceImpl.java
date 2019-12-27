package com.waylon.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.waylon.dao.CheckGroupDao;
import com.waylon.dao.SetmealDao;
import com.waylon.pojo.Setmeal;
import com.waylon.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        //设置套餐跟检查组的关系表
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);

    }

    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkGroupId", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

}
