package com.jk.controller;

import ch.qos.logback.core.util.TimeUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.model.Tree;
import com.jk.model.User;
import com.jk.service.TreeService;
import com.jk.util.TreeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("tree")
public class TreeController {
    @Reference
    private TreeService tr;

        //redis  注入
        @Resource
        private RedisTemplate redisTemplate;




    //树查询
    @RequestMapping("getTree")
    @ResponseBody
    public List<Tree>  grtTree(HttpServletRequest request){
       User user= (User)request.getSession().getAttribute("user");  //权限 按登录用户展示
        List<Tree> list=new  ArrayList<Tree>();
        String key="tree"+user.getId();
        if(redisTemplate.hasKey(key)){
            System.out.println("缓存");
            list= (List<Tree>) redisTemplate.opsForValue().get(key);
        }else{
            System.out.println("数据库");
            list=tr.getTree(user.getId());
            list= TreeUtil.getFatherNode(list);
            redisTemplate.opsForValue().set(key,list);
           redisTemplate.expire(key,30, TimeUnit.MINUTES);
        }

        return  list;
    }





}
