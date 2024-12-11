package com.example.demo.service;

import com.example.demo.mapper.ReplyMapper;
import com.example.demo.model.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    public List<Reply> getRepliesByMessageId(Long messageId) {
        return replyMapper.findRepliesByMessageId(messageId);
    }

    public Reply getReplyById(Long replyId) {
        return replyMapper.getReplyById(replyId);
    }

    public void saveReply(Reply reply) {
        replyMapper.saveReply(reply);
    }

    public void deleteReply(Long id) {
        replyMapper.deleteReply(id);
    }

    public List<Reply> getRepliesByUserId(Long userId) {
        return replyMapper.findRepliesByUserId(userId);
    }


}
