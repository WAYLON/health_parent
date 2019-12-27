package com.waylon.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.waylon.dao.SetmealDao;
import com.waylon.entity.PageResult;
import com.waylon.entity.QueryPageBean;
import com.waylon.pojo.Setmeal;
import com.waylon.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
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

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //分页助手插件
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> result = page.getResult();
        return new PageResult(total, result);
    }

    @Override
    public void deleteById(Integer id) {
        //根据套餐id删除中间表数据（清理原有关联关系）
        setmealDao.deleteAssociation(id);
        setmealDao.deleteById(id);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //根据套餐id删除中间表数据（清理原有关联关系）
        setmealDao.deleteAssociation(setmeal.getId());
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        //更新套餐基本信息
        setmealDao.edit(setmeal);
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
