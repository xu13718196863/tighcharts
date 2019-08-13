package com.jk.service;

import com.jk.mapper.TighchartsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TighchartsServiceImpl implements TighchartsService{
    @Autowired
    private TighchartsMapper  t;

    // 1
    @Override
    public List<Map<String, Object>> tig1() {
        return t.tig1();
    }

    //2
    @Override
    public List<Map<String, Object>> tig2() {
        return t.tig2();
    }

    //3
    @Override
    public List<Map<String, Object>> tig3() {
        return t.tig3();
    }

    //4
    @Override
    public List<Map<String, Object>> tig4() {
        return t.tig4();
    }
}
