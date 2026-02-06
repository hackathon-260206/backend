package com.example.app.controller;

import com.example.app.dto.ChatMessageResponse;
import com.example.app.dto.ChatReadRequest;
import com.example.app.dto.ChatReadResponse;
import com.example.app.dto.ChatSendRequest;
import com.example.app.exception.AuthRequiredException;
import com.example.app.service.ChatService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatSocketController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void send(@Valid ChatSendRequest request, SimpMessageHeaderAccessor accessor) {
        Long senderId = getSessionUserId(accessor);
        ChatMessageResponse response = chatService.sendMessage(senderId, request.getReceiverId(), request.getContent());
        messagingTemplate.convertAndSend("/topic/chat/" + request.getReceiverId(), response);
        messagingTemplate.convertAndSend("/topic/chat/" + senderId, response);
    }

    @MessageMapping("/chat.read")
    public void read(@Valid ChatReadRequest request, SimpMessageHeaderAccessor accessor) {
        Long readerId = getSessionUserId(accessor);
        ChatReadResponse response = chatService.markRead(readerId, request.getPartnerId());
        messagingTemplate.convertAndSend("/topic/chat.read/" + readerId, response);
        messagingTemplate.convertAndSend("/topic/chat.read/" + request.getPartnerId(), response);
    }

    private Long getSessionUserId(SimpMessageHeaderAccessor accessor) {
        Map<String, Object> attrs = accessor.getSessionAttributes();
        if (attrs == null || !attrs.containsKey("USER_ID")) {
            throw new AuthRequiredException("Login required");
        }
        Object id = attrs.get("USER_ID");
        if (!(id instanceof Long)) {
            throw new AuthRequiredException("Login required");
        }
        return (Long) id;
    }
}