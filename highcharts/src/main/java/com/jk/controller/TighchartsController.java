package com.jk.controller;


import com.jk.service.TighchartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TighchartsController {

    @Autowired
    private TighchartsService t;

    //跳tighcharts1页面
    @RequestMapping("tighcharts1")
    public String tighcharts1(){
        return "tighcharts1";
    }
    //跳tighcharts2页面
    @RequestMapping("tighcharts2")
    public String tighcharts2(){
        return "tighcharts2";
    }
    //跳tighcharts3页面
    @RequestMapping("tighcharts3")
    public String tighcharts3(){
        return "tighcharts3";
    }
    //跳tighcharts4页面
    @RequestMapping("tighcharts4")
    public String tighcharts4(){
        return "tighcharts4";
    }


    //图示一折线图(曲线)
    @RequestMapping("tig1")
    @ResponseBody
    public  List<Map<String,Object>>   tig1(){
        System.out.println(456);
        System.out.println(225);
        List<Map<String,Object>> list=t.tig1();
        List<Map<String,Object>>  map=new ArrayList<>();

        Map<String,Object>  m1=new HashMap<>();
        Map<String,Object>  m2=new HashMap<>();
        Map<String,Object>  m3=new HashMap<>();
        int [] a1 =new int[(int)list.size()];
        int [] a2 =new int[list.size()];
       int [] a3 =new int[list.size()];
        int i1=0;
        int i2=0;
        int i3=0;
        for (Map<String,Object>  m:list ) {
            Integer  o = Integer.parseInt(m.get("数量").toString()) ;
          if(m.get("类型").equals("小说")){
               a1[i1] =o;
              i1++;
          }else if(m.get("类型").equals("历史")){
              a2[i2] =o;
              i2++;
            }else  if(m.get("类型").equals("名著")){
              a3[i3] =o;
              i3++;
            }
        }
        m1.put("name","小说");
        m1.put("data",a1);
        m2.put("name","历史");
        m2.put("data",a2);
        m3.put("name","名著");
        m3.put("data",a3);
        map.add(m1);
        map.add(m2);
        map.add(m3);
        return  map;
    }


    //图示二饼型图(圆形)
    @RequestMapping("tig2")
    @ResponseBody
    public  List<Map<String,Object>>   tig2(){
        List<Map<String,Object>> list= t.tig2();
        List<Map<String,Object>>  map=new ArrayList<>();

        for (Map<String,Object> m:list) {
            Map<String,Object>  ma=new HashMap<>();
              ma.put("name",m.get("月份")+"月份");
              ma.put("y",m.get("数量"));
              map.add(ma);
        }
        return  map;
    }


    //图示三 3D柱形图(3D柱形)
    @RequestMapping("tig3")
    @ResponseBody
    public  List<Map<String,Object>>   tig3(){
        List<Map<String,Object>> list= t.tig3();
        List<Map<String,Object>>  map=new ArrayList<>();
        Map<String,Object>  m1=new HashMap<>();
           int[]  aa=new  int[list.size()];
           int  i=0;
        for (Map<String,Object>  m:list ) {
            Integer  o = Integer.parseInt(m.get("数量").toString()) ;
               aa[i]=o;
                i++;
        }
          m1.put("name","每周内每日数量图");
          m1.put("data",aa);
          map.add(m1);
          return  map;

    }


    //图示四 蜘蛛图(网状)
    @RequestMapping("tig4")
    @ResponseBody
    public  List<Map<String,Object>>   tig4(){
        List<Map<String,Object>> list=t.tig4();
        List<Map<String,Object>>  map=new ArrayList<>();

        Map<String,Object>  m1=new HashMap<>();
        Map<String,Object>  m2=new HashMap<>();
        Map<String,Object>  m3=new HashMap<>();
        int [] a1 =new int[(int)list.size()];
        int [] a2 =new int[list.size()];
        int [] a3 =new int[list.size()];
        int i1=0;
        int i2=0;
        int i3=0;
        for (Map<String,Object>  m:list ) {
            Integer  o = Integer.parseInt(m.get("数量").toString()) ;
            if(m.get("类型").equals("小说")){
                a1[i1] =o;
                i1++;
            }else if(m.get("类型").equals("历史")){
                a2[i2] =o;
                i2++;
            }else  if(m.get("类型").equals("名著")){
                a3[i3] =o;
                i3++;
            }
        }
        System.out.println(a1[1]);
        m1.put("type","column");
        m1.put("name","小说");
        m1.put("data",a1);

        m2.put("type","line");
        m2.put("name","历史");
        m2.put("data",a2);

        m3.put("type","area");
        m3.put("name","名著");
        m3.put("data",a3);
        map.add(m1);
        map.add(m2);
        map.add(m3);
        return  map;
    }
}
