package com.jk.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jk.dao.QuanXianDao;
import com.jk.model.Jiaose;
import com.jk.model.Tree;
import com.jk.model.User;
import com.jk.util.BootStrapUtil;
import com.jk.util.RowsTotal;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuanXianServiceImpl implements QuanXianService{
   @Autowired
   private QuanXianDao qx;


   //用户查询
    @Override
    public RowsTotal yonghu(BootStrapUtil bt) {
         Map map=new HashMap();
          Integer  total=qx.count1();  //总条数
        map.put("sta",(bt.getPageNumber()-1)*bt.getPageSize());
        map.put("end",bt.getPageSize());
        List<User>  list=qx.yonghu(map);
        RowsTotal rt=new RowsTotal();
        rt.setTotal(total);
        rt.setRows(list);
        return rt;
    }

    //角色查询
    @Override
    public RowsTotal jiaose(BootStrapUtil bt) {
        Map map=new HashMap();
        Integer  total=qx.count2();  //总条数
        map.put("sta",(bt.getPageNumber()-1)*bt.getPageSize());
        map.put("end",bt.getPageSize());
        List<Jiaose>  list=qx.jiaose(map);
        RowsTotal rt=new RowsTotal();
        rt.setTotal(total);
        rt.setRows(list);
        return rt;
    }

    //用户 查角色 回显
    @Override
    public List<Jiaose> yonghu1(Integer id) {
        //查出  用户 id  所对应的 角色 的id
        List<Integer> list1= qx.yhSelect1(id);
        // 查询所有 的角色
        List<Jiaose>    list2=qx.yhSelect2();

        //将对应id选中回显
        for (int i = 0; i <list1.size(); i++) {  //对应的 角色 的id
            for (int j = 0; j < list2.size(); j++) {  //角色所用
                if(list2.get(j).getId()==list1.get(i)) {
                    list2.get(j).setChecked("true");
                }
            }
        }

        return list2;
    }

    //角色 查 菜单 回显
    @Override
    public List<Tree> jiaose1(Integer id) {
        //查出  角色 id  所对应的 菜单 的id
        List<Integer> list1= qx.jeSelect1(id);
        // 查询所有 的菜单
        List<Tree>    list2=qx.jeSelect2();

        //将对应id选中回显
        for (int i = 0; i <list1.size(); i++) {  //对应的 菜单 的id
            for (int j = 0; j < list2.size(); j++) {  //菜单所用
                if(list2.get(j).getId()==list1.get(i)) {
                    list2.get(j).setChecked("true");
                }
            }
        }

        return list2;
    }

    //用户改（先删后增）
    @Override
    public void updateyh(String[] ids, Integer uid) {
    //先 删除 原有的 关联
        int a=  qx.delete3(uid);
        //再建立  新的 关联 增
        if(a>=0) {
            for (int i = 0; i < ids.length; i++) {
                qx.add1(ids[i],uid);
            }
        }
    }

    // 角色改（先删后增）
    @Override
    public void updatejs(String[] ids, Integer jid) {

        //先 删除 原有的 关联
        int a=  qx.delete2(jid);
        //再建立  新的 关联 增
        if(a>=0) {
            for (int i = 0; i < ids.length; i++) {

                Map map=new HashMap();
                map.put("tid",ids[i]);
                map.put("jid",jid);
                qx.add2(map);
            }
        }
    }


}
