package com.example.demo.mapper;

import com.example.demo.model.Reply;
import org.apache.ibatis.annotations.*;
import com.example.demo.model.Message;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MessageMapper {
    @Insert("INSERT INTO messages(userId, title, content, category) VALUES(#{userId}, #{title}, #{content}, #{category})")
    void saveMessage(Message message);

    @Select("SELECT * FROM messages ORDER BY createdAt DESC")
    List<Message> findAllMessages();

    @Select("SELECT * FROM messages WHERE userId = #{userId}")
    List<Message> findMessagesByUser(Long userId);

    @Select("SELECT * FROM messages WHERE title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')")
    List<Message> searchMessagesByKeyword(String keyword);

    @Select("SELECT * FROM messages WHERE id = #{id}")
    Message findMessageById(Long id);

    @Update("UPDATE messages SET title = #{title}, content = #{content}, category = #{category} WHERE id = #{id}")
    void updateMessage(Message message);

    @Delete("DELETE FROM messages WHERE id = #{id}")
    void deleteMessage(Long id);

    @Select("SELECT * FROM messages WHERE category = #{category}")
    List<Message> findMessagesByCategory(String category);

    // ReplyMapper.java
    @Select("SELECT * FROM replies WHERE userId = #{userId}")
    List<Reply> findRepliesByUserId(Long userId);

    // MessageMapper.java
    @Select("SELECT * FROM messages")
    List<Message> findAllMessagesWithRepliesCount();

    @Select("SELECT COUNT(*) FROM replies WHERE messageId = #{id}")
    int getMessageRepliesCount(Long id);

    // MessageMapper.java
    @Select("SELECT * FROM messages WHERE createdAt BETWEEN #{startTime} AND #{endTime}")
    List<Message> findMessagesByCreatedAtRange(LocalDateTime startTime, LocalDateTime endTime);
}
