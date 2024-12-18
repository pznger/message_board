package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword, Long userId) {
        User user = userMapper.findByUsername(userMapper.findUserById(userId).getUsername());
        if (user != null && user.getPassword().equals(oldPassword)) {
            if (newPassword.equals(confirmPassword)) {
                user.setPassword(newPassword);
                userMapper.updateUserPassword(user);
            } else {
                throw new RuntimeException("新密码和确认密码不匹配");
            }
        } else {
            throw new RuntimeException("旧密码错误");
        }
    }

    // UserService.java
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = userMapper.findUserById(userId);
        System.out.println("avatarUrl"+avatarUrl);
        user.setAvatarUrl(avatarUrl);
        userMapper.updateUserAvatar(user);
    }

    // UserService.java
    public void updateUserEmail(Long userId, String email) {
        User user = userMapper.findUserById(userId);
        user.setEmail(email);
        userMapper.updateUserEmail(user);
    }

    public User findUserById(Long id) {
        return userMapper.findUserById(id);
    }

    public boolean hasSignInToday(Long id) {
        String lastSignInDateStr = userMapper.getLastSignInDate(id);
        if (lastSignInDateStr == null) return false;

        LocalDate lastSignInDate = LocalDate.parse(lastSignInDateStr);
        return lastSignInDate.isEqual(LocalDate.now());
    }

    public void incrementSignInDays(Long id) {
        if (!hasSignInToday(id)) {
            LocalDate now = LocalDate.now();
            Date nowDate = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
            userMapper.updateSignInDaysAndLastSignInDate(id, nowDate);
        }
    }
}
