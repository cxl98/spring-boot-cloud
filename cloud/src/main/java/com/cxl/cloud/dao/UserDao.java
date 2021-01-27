package com.cxl.cloud.dao;

import com.cxl.cloud.entry.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    @Select("select * from user where username=#{username}")
    User selectUserByUsername(String username);
    @Insert("insert into user(username,password,roleCode,gmtCreate) values(#{username},#{password},#{roleCode},#{gmtCreate})")
    int insert(User user);
}
