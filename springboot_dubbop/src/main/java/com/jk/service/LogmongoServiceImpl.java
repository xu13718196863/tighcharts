package com.jk.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jk.model.Log;
import com.jk.util.BootStrapUtil;
import com.jk.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class LogmongoServiceImpl  implements LogmongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    //查
    @Override
    public PageUtil listlog(BootStrapUtil bt) {
        Criteria c=new  Criteria();    //设置条件
       /* if(bt.getName()!=null && !bt.getName().equals(" ")){
            Pattern pattern = Pattern.compile("^.*"+bt.getName()+".*$", Pattern.CASE_INSENSITIVE); //模糊查转换
            c.and("logname").regex(pattern);   //模糊查

        }*/
        Query query =new  Query();   //使条件生效
        query.addCriteria( c );

        //分页
        //count 查总条数 （返回是long 需转换）
        long count=   mongoTemplate.count(query,Log.class,"log" );

        PageUtil  pa=new  PageUtil((int)count,bt.getPageNumber(),bt.getPageSize());
        Integer sta = pa.getFirstResultCount();   //开始条数	   （或 sta=(当前页-1)*每页几条）
        query.skip(sta);  //开始条数  （跳过几条后开始）
        query.limit(bt.getPageSize());	//每页几条
        List<Log> list = mongoTemplate.find(query, Log.class, "log");

        pa.setList(list);
        System.out.println(789);
        return pa;
    }

    //修
    @Override
    public void addlog(Log log) {
           Criteria c=new Criteria();
           c.and("_id").is(log.getId());
           Query query=new Query();
           query.addCriteria(c);
        Update u=new Update();
        u.set("logname",log.getLogname());
        u.set("logip",log.getLogip());
        u.set("logdate",log.getLogdate());
        u.set("parame",log.getParame());
        mongoTemplate.updateFirst (query,u,"log");

    }

    //删  批删
    @Override
    public void delete1(String[] ids) {
        Criteria c=new Criteria();
        c.and("_id").in(ids);
        Query query=new Query();
        query.addCriteria(c);
        mongoTemplate.remove(query,"log");
    }
}
