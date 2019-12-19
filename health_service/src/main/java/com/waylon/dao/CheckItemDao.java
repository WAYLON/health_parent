package com.waylon.dao;

import com.github.pagehelper.Page;
import com.waylon.entity.PageResult;
import com.waylon.pojo.CheckItem;

/**
 * 检查项dao接口
 *
 * @author 80481
 */
public interface CheckItemDao {

    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public void deleteById(Integer id);

    public long findCountByCheckItemId(Integer checkItemId);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);
}
