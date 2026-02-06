package com.example.app.repository;

import com.example.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query("update Message m set m.isRead = true where m.sender.id = :senderId and m.receiver.id = :receiverId and m.isRead = false")
    int markAsRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}