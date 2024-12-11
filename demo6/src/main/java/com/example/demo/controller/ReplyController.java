package com.example.demo.controller;

import com.example.demo.model.Reply;
import com.example.demo.model.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/add")
    public String addReply(@RequestParam Long messageId, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Reply reply = new Reply();
            reply.setMessageId(messageId);
            reply.setUserId(user.getId());
            reply.setContent(content);
            replyService.saveReply(reply);
        }
        return "redirect:/message/detail/" + messageId;
    }

    @GetMapping("/delete")
    public String deleteReply(@RequestParam Long replyId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Reply reply = replyService.getReplyById(replyId);

        if (user != null) {
            Reply reply2 = replyService.getReplyById(replyId);
            if (reply2 != null && reply2.getUserId().equals(user.getId())) {
                replyService.deleteReply(replyId);
            }
        }
        return "redirect:/message/detail/" + reply.getMessageId();
    }

    private Reply getReplyById(Long replyId) {
        return replyService.getReplyById(replyId);
    }
}