package com.jk.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jk.dao.TreeDao;
import com.jk.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TreeServiceImpl implements TreeService {
   @Autowired
   private TreeDao tr;

    @Override
    public List<Tree> getTree(Integer id) {

        return tr.getTree(id);
    }
}
