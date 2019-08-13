package com.jk.dao;

import com.jk.model.Tree;

import java.util.List;

public interface ExDao {
    List<Tree> exquery();

    void add(List<Tree> list);
}
