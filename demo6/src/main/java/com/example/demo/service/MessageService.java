package com.example.demo.service;

import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.ReplyMapper;
import com.example.demo.model.Message;
import com.example.demo.model.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ReplyMapper replyMapper;

    public List<Message> getAllMessages() {
        return messageMapper.findAllMessages();
    }

    public List<Message> getMessagesByUser(Long userId) {
        return messageMapper.findMessagesByUser(userId);
    }

    public List<Message> searchMessagesByKeyword(String keyword) {
        return messageMapper.searchMessagesByKeyword(keyword);
    }

    public Message getMessageById(Long id) {
        return messageMapper.findMessageById(id);
    }

    public void saveMessage(Message message) {
        messageMapper.saveMessage(message);
    }

    public void updateMessage(Message message) {
        messageMapper.updateMessage(message);
    }

    public void deleteMessage(Long id) {
        // 首先获取所有相关的回复
        List<Reply> replies = replyMapper.findRepliesByMessageId(id);
        // 删除每个回复
        for (Reply reply : replies) {
            replyMapper.deleteReply(reply.getId());
        }
        // 删除留言
        messageMapper.deleteMessage(id);
    }
    // MessageService.java
    public List<Reply> getRepliesByMessageId(Long messageId) {
        return replyMapper.findRepliesByMessageId(messageId);
    }

    public List<Message> getMessagesByCategory(String category) {
        return messageMapper.findMessagesByCategory(category);
    }

    // MessageService.java
    public Map<Long, Integer> getMessageRepliesCount() {
        List<Reply> allReplies = replyMapper.findAllReplies();
        Map<Long, Integer> repliesCount = new HashMap<>();
        for (Reply reply : allReplies) {
            Long messageId = reply.getMessageId();
            repliesCount.put(messageId, repliesCount.getOrDefault(messageId, 0) + 1);
        }
        return repliesCount;
    }

    // 更新 getMessageRepliesCount 方法，返回 Map
    public Map<Long, Integer> getMessageRepliesCountMap() {
        List<Reply> allReplies = replyMapper.findAllReplies();
        Map<Long, Integer> repliesCountMap = new HashMap<>();
        for (Reply reply : allReplies) {
            Long messageId = reply.getMessageId();
            repliesCountMap.put(messageId, repliesCountMap.getOrDefault(messageId, 0) + 1);
        }
        return repliesCountMap;
    }

    public List<Message> getMessagesWithRepliesCount() {
        List<Message> messages = messageMapper.findAllMessages();
        Map<Long, Integer> repliesCountMap = getMessageRepliesCount();
        for (Message message : messages) {
            message.setRepliesCount(repliesCountMap.getOrDefault(message.getId(), 0));
        }
        return messages;
    }

    // MessageService.java
    public List<Message> getTopTodayMessages() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(1);
        List<Message> messages = messageMapper.findMessagesByCreatedAtRange(startTime, now);
        Map<Long, Integer> repliesCountMap = getMessageRepliesCountMap();
        List<Message> topMessages = messages.stream()
                .filter(message -> message.getCreatedAt().isAfter(startTime) && message.getCreatedAt().isBefore(now))
                .sorted(Comparator.comparingInt((Message m) -> repliesCountMap.getOrDefault(m.getId(), 0)).reversed())
                .limit(10)
                .toList();
        topMessages.forEach(message -> message.setRepliesCount(repliesCountMap.getOrDefault(message.getId(), 0)));
        return topMessages;
    }

}
