package com.jk.dao;

import com.jk.model.Tree;

import java.util.List;

public interface TreeDao {
    List<Tree> getTree(Integer id);
}
