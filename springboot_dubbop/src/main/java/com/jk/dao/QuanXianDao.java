package com.jk.dao;

import com.jk.model.Jiaose;
import com.jk.model.Tree;
import com.jk.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface QuanXianDao {
    @Select("select count(1) from user5")
    Integer count1();

    @Select("select * from user5 limit #{sta},#{end} ")
    List<User> yonghu(Map map);



    @Select("select count(1) from jiaose5")
    Integer count2();

    @Select("select * from jiaose5  limit #{sta},#{end}")
    List<Jiaose> jiaose(Map map);

    @Select("select tid from j_t5 where jid=#{jid} ")
    List<Integer> jeSelect1(Integer id);

    @Select("select * from tree5 ")
    List<Tree> jeSelect2();

    @Select("select jid from u_j5 where uid=#{uid} ")
    List<Integer> yhSelect1(Integer id);

    @Select("select * from jiaose5 ")
    List<Jiaose> yhSelect2();



    @Delete("delete from j_t5 where jid=#{jid}")
    int delete2(Integer jid);

    @Insert ("insert into j_t5(jid,tid) values(#{jid},#{tid} ) ")
    void add2(Map map);

    @Delete("delete from u_j5 where uid=#{uid}")
    int delete3(Integer uid);

    @Insert("insert into u_j5(jid,uid) values(#{0},#{1} ) ")
    void add1(String string, Integer uid);


}
