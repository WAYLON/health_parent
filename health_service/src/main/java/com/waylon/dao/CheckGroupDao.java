package com.waylon.dao;

import com.github.pagehelper.Page;
import com.waylon.pojo.CheckGroup;

import java.util.Map;

public interface CheckGroupDao {

    public void add(CheckGroup checkGroup);

    public void setCheckGroupAndCheckItem(Map<String, Integer> map);

    public Page<CheckGroup> selectByCondition(String queryString);
}
