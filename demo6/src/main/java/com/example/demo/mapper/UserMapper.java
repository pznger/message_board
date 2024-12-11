package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface UserMapper {

    // UserMapper.java
    @Insert("INSERT INTO users(username, password, email, avatarUrl) VALUES(#{username}, #{password}, #{email}, #{avatarUrl})")
    void saveUser(User user);

    // UserMapper.java
    @Update("UPDATE users SET avatarUrl = #{avatarUrl} WHERE id = #{id}")
    void updateUserAvatar(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findUserById(Long id);

    @Update("UPDATE users SET password = #{password} WHERE id = #{id}")
    void updateUserPassword(User user);

    // UserMapper.java
    @Update("UPDATE users SET email = #{email} WHERE id = #{id}")
    void updateUserEmail(User user);

    @Update("UPDATE users SET signInDays = signInDays + 1 WHERE id = #{id}")
    void incrementSignInDays(@Param("id") Long id);

    @Select("SELECT signInDays FROM users WHERE id = #{id}")
    int getSignInDays(@Param("id") Long id);



    @Select("SELECT lastSignInDate FROM users WHERE id = #{id}")
    String getLastSignInDate(@Param("id") Long id);

    @Update("UPDATE users SET signInDays = signInDays + 1, lastSignInDate = #{lastSignInDate} WHERE id = #{id}")
    void updateSignInDaysAndLastSignInDate(@Param("id") Long id, @Param("lastSignInDate") Date lastSignInDate);

    @Update("UPDATE users SET lastSignInDate = #{lastSignInDate} WHERE id = #{id}")
    void updateLastSignInDate(@Param("id") Long id, @Param("lastSignInDate") String lastSignInDate);
}
