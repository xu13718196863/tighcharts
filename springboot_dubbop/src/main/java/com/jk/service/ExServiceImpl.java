package com.jk.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jk.dao.ExDao;
import com.jk.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExServiceImpl implements  ExService {
   @Autowired
   private ExDao ex;

    @Override
    public List<Tree> exquery() {
        return ex.exquery();
    }

    @Override
    public void add(List<Tree> list) {
        ex.add(list);
    }
}
