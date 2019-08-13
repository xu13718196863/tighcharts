package com.jk.controller;

import com.jk.model.Log;
import com.jk.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

//加入注解
@Aspect    //声明Aop
@Component
public class Logadd {  //mongo 自增 log日志信息
    //注入mongodb
    @Autowired
    private MongoTemplate mongoTemplate;;

    //设置要拦截的层（这里只拦UserContrller请求，可以每一层都拦，也可精确到拦一个方法）
    @Pointcut("execution(* com.jk.controller.UserController.denglu(..))")
    public  void  logPointCut() { }  //定义一个方(根据注解获取请求地址供下面使用)

    //本方法用了 aop切点中的：  后置通知
    //JoinPoint包导进来的  value=“和上面的方法名一致” ,returning="returningValue返回值 和下面参数一致"
    @AfterReturning(value="logPointCut()",returning="returningValue")
    public  void  logadd(JoinPoint jp, Object returningValue) throws UnknownHostException{ //set获取ip抛出的异常  //returningValue是返回值，但需要告诉spring
        System.out.println("《注解形式-后置通知》：目标对象请求地址："+jp.getTarget()+",方法名："+jp.getSignature().getName() +",参数列表："+  jp.getArgs().length+",返回值："+returningValue );

        //new 实体Log  来装对应的内容  （这里7个属性）
        Log log=new Log();

        log.setLogip(getIp());    //getIp()调用下面的方法获取IP地址信息
        log.setLogdate(new Date());   //添加当前时间
        log.setReturnValue(returningValue);  //返回值（执行成功后返回的值）
        //jp.getTarget().getClass();获取 目标 对象   jp.getSignature().getName();获取方法名
        log.setSetrequerpath(jp.getTarget().getClass() +"/"+ jp.getSignature().getName());

        //jp.getTarget().getClass()获取com.--UserController太长 可以截取后一部分
        String name = jp.getTarget().getClass().toString().substring( jp.getTarget().getClass().toString().lastIndexOf(".")+1 );
        //每个请求路径都可给一个日志名称
        if(name.equals("UserController")) { //判断 如果请求的是这层路径就给一个自定的名称
            log.setLogname("用户日志");  //定义一个日志名称
        }

        //获取请求参数  jp.getArgs();
        Object[] args=jp.getArgs();
        if(args != null) {
            //new StringBuffer ，也可定义String aa=""; 进行拼接
            StringBuffer requestParams = new StringBuffer();
            //循环获取参数
            for (int i = 0; i < args.length; i++) {
                requestParams.append("第"+i+"个参数：").append(args[i].toString());
            }
            log.setParame(requestParams.toString());  //请求参数
        }

        // 获取  request 对象  来获取  session ，通过session获取用户信息
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user = (User) request.getSession().getAttribute("user");
        if(user !=null) {
            log.setUserid(user.getId()); //获取用户id，后期可根据用户ID权限查询日志信息

        }


        //新增
        if(log != null) {
            //insert新增   将log实体类 增到mongodb库的log表  （mongo可以批增直接放集合（将多个log对象放入集合增））
            mongoTemplate.insert(log, "log");
        }

    }




    /**
     *     获取  ip 地址
     * @return
     * @throws UnknownHostException
     */
    private String getIp() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }


}

