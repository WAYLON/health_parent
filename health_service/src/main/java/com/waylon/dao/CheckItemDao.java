package com.waylon.dao;

import com.github.pagehelper.Page;
import com.waylon.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public void deleteById(Integer id);

    public long findCountByCheckItemId(Integer checkItemId);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    public List<CheckItem> findAll();

    public List<CheckItem> findCheckItemById(Integer id);
}
