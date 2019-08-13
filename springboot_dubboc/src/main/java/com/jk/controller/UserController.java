package com.jk.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.model.User;
import com.jk.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController {
       @Reference
       private UserService us;

       //转登录页面
    @RequestMapping("a")
    public String deng(){
        return  "denglu";
    }
    //登录成功跳布局
    @RequestMapping("bootstrap")
    public String bootstrap(){
        return  "bootstrap";
    }
   //转用户页面
    @RequestMapping("yonghu")
    public String yonghu(){
        return  "yonghu";
    }
    //转角色页面
    @RequestMapping("jiaose")
    public String jiaose(){
        return  "jiaose";
    }
    //转角日志页面
    @RequestMapping("listlog")
    public String listlog(){
        return  "listlog";
    }



    //登录
    @RequestMapping("denglu")
    @ResponseBody
    public String denglu(User user, HttpServletRequest request){

       User uu= us.denglu(user.getUsername());
       if(uu==null){
           return "error1"; //账号错误
       }
       if (! uu.getUserpass().equals(user.getUserpass())){
           return "error2";  //密码错误
       }

        request.getSession().setAttribute("user",uu);  //存入session
        return  "success";  //成功
    }


}
