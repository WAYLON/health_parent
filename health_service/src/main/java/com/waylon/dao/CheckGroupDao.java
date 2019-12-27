package com.waylon.dao;

import com.github.pagehelper.Page;
import com.waylon.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    public void add(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map<String, Integer> map);

    public Page<CheckGroup> selectByCondition(String queryString);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public void deleteAssociation(Integer id);

    public void edit(CheckGroup checkGroup);

    public void deleteById(Integer id);

    public List<CheckGroup> findAll();

}
