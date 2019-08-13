package com.jk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.model.Jiaose;
import com.jk.model.Tree;
import com.jk.model.User;
import com.jk.service.QuanXianService;
import com.jk.util.BootStrapUtil;
import com.jk.util.RowsTotal;
import com.jk.util.TreeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("qx")
public class QuanXianController {
    @Reference
    private QuanXianService qx;


    //用户查询
    @RequestMapping("yonghu")
    @ResponseBody
    public RowsTotal yonghu(@RequestBody BootStrapUtil bt){
        return  qx.yonghu(bt);
    }

    //角色查询
    @RequestMapping("jiaose")
    @ResponseBody
    public RowsTotal jiaose(@RequestBody  BootStrapUtil bt){

        return  qx.jiaose(bt);
    }



    //用户 查角色 回显
    @RequestMapping("yonghu1")
    @ResponseBody
    public List<Jiaose> yonghu1(Integer id){
        List<Jiaose> list=qx.yonghu1(id);
        return  list;
    }

    //角色 查 菜单 回显
    @RequestMapping("jiaose1")
    @ResponseBody
    public List<Tree> jiaose1(Integer id){
        List<Tree> list=qx.jiaose1(id);
        list= TreeUtil.getFatherNode(list);
        return  list;
    }



    //用户改（增删）
    @RequestMapping("updateyh")
    @ResponseBody
    public void updateyh(String[] ids,Integer uid){
        qx.updateyh(ids,uid);

    }

    // 角色改 （增删）
    @RequestMapping("updatejs")
    @ResponseBody
    public void updatejs(String[] ids,Integer jid){
        qx.updatejs(ids,jid);
    }
}
