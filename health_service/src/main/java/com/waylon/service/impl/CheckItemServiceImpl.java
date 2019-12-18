package com.waylon.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.waylon.pojo.CheckItem;
import com.waylon.service.CheckItemService;
import com.waylon.dao.CheckItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检查项实现类
 *
 * @author 80481
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
