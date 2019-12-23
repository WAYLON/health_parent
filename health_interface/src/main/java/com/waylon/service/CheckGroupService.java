package com.waylon.service;

import com.waylon.entity.PageResult;
import com.waylon.entity.QueryPageBean;
import com.waylon.pojo.CheckGroup;

public interface CheckGroupService {

    public void add(CheckGroup checkGroup, Integer[] checkItemIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);
}
