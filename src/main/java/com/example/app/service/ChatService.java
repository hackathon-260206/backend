package com.example.app.service;

import com.example.app.dto.ChatMessageResponse;
import com.example.app.dto.ChatReadResponse;
import com.example.app.entity.Message;
import com.example.app.entity.User;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.MessageRepository;
import com.example.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChatMessageResponse sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("User not found: " + senderId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new NotFoundException("User not found: " + receiverId));

        Message message = new Message(sender, receiver, content);
        Message saved = messageRepository.save(message);

        return new ChatMessageResponse(
                saved.getId(),
                senderId,
                receiverId,
                saved.getContent(),
                Boolean.TRUE.equals(saved.getIsRead()),
                saved.getCreatedAt()
        );
    }

    @Transactional
    public ChatReadResponse markRead(Long readerId, Long partnerId) {
        messageRepository.markAsRead(partnerId, readerId);
        return new ChatReadResponse(partnerId, readerId, "read");
    }
}