package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Reply;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.FileService;
import com.example.demo.service.ReplyService;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileService fileService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());  // 添加User对象到模型中
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, @RequestParam String captcha, HttpSession session) {
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha != null && sessionCaptcha.equalsIgnoreCase(captcha)) {
            User user = userService.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                return "redirect:/message/list";
            }
        }
        session.setAttribute("error", "用户名或密码错误，请重试。");
        return "redirect:/user/login";
    }

    // UserController.java
    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Message> userMessages = messageService.getMessagesByUser(user.getId());
            List<Reply> userReplies = replyService.getRepliesByUserId(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("userMessages", userMessages);
            model.addAttribute("userReplies", userReplies);
            return "profile";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, HttpSession session) {
        Long userId = ((User) session.getAttribute("user")).getId();
        try {
            userService.changePassword(oldPassword, newPassword, confirmPassword, userId);
            return "redirect:/user/profile";
        } catch (RuntimeException e) {
            // 处理错误，例如重定向回更改密码页面并显示错误消息
            return "redirect:/user/change-password?error=" + e.getMessage();
        }
    }

    @PostMapping("/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            String avatarUrl = fileService.uploadFile(avatarFile);
            // 添加一个时间戳参数来防止缓存
            String uniqueAvatarUrl = avatarUrl + "?t=" + System.currentTimeMillis();
            user.setAvatarUrl(uniqueAvatarUrl);
            userService.updateAvatar(user.getId(), uniqueAvatarUrl);
        } catch (IOException e) {
            // 处理错误
        }
        return "redirect:/user/profile";
    }

    // UserController.java
    @PostMapping("/update-email")
    public String updateEmail(@RequestParam String email, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            user.setEmail(email);
            userService.updateUserEmail(user.getId(), email);
            return "redirect:/user/profile";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/sign-in")
    public String signInPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            boolean hasSignInToday = userService.hasSignInToday(user.getId());
            model.addAttribute("todaySignedIn", hasSignInToday);
        }
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (!userService.hasSignInToday(user.getId())) {
                userService.incrementSignInDays(user.getId());
                // 重新从数据库加载用户信息，以确保获取最新的签到天数
                user = userService.findUserById(user.getId());
                session.setAttribute("user", user); // 更新Session中的用户信息
            }
            return "redirect:/user/sign-in";
        }
        return "redirect:/user/login";
    }
}
