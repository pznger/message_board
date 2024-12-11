package com.example.demo.mapper;

import com.example.demo.model.Message;
import com.example.demo.model.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReplyMapper {
    @Select("SELECT * FROM replies WHERE messageId = #{messageId}")
    List<Reply> findRepliesByMessageId(Long messageId);

    @Insert("INSERT INTO replies(messageId, userId, content) VALUES(#{messageId}, #{userId}, #{content})")
    void saveReply(Reply reply);

    @Delete("DELETE FROM replies WHERE id = #{id}")
    void deleteReply(Long id);

    @Select("SELECT * FROM replies WHERE id = #{id}")
    Reply getReplyById(Long id);

    // MessageMapper.java
    @Select("SELECT * FROM messages WHERE userId = #{userId}")
    List<Message> findMessagesByUser(Long userId);

    // ReplyMapper.java
    @Select("SELECT * FROM replies WHERE userId = #{userId}")
    List<Reply> findRepliesByUserId(Long userId);

    // ReplyMapper.java
    @Select("SELECT * FROM replies")
    List<Reply> findAllReplies();

    // 新增方法，返回 Map
    @Select("SELECT messageId, COUNT(*) FROM replies GROUP BY messageId")
    @Results({
            @Result(property = "messageId", column = "messageId"),
            @Result(property = "count", column = "COUNT(*)")
    })
    List<ReplyCount> findReplyCounts();

    @Select("SELECT messageId, COUNT(*) as count FROM replies GROUP BY messageId")
    List<ReplyCount> getMessageRepliesCount();

    // 定义一个内部类用于接收查询结果
    class ReplyCount {
        private Long messageId;
        private Integer count;
        // getter 和 setter 方法
        public Long getMessageId() {
            return messageId;
        }
        public void setMessageId(Long messageId) {
            this.messageId = messageId;
        }
        public Integer getCount() {
            return count;
        }
        public void setCount(Integer count) {
            this.count = count;
        }
    }
}