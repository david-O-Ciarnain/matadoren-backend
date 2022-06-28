package com.team7.matadorenbackend.websockets.controller;

import com.team7.matadorenbackend.websockets.model.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class ChatController {

    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage sendMessage(@Payload final ChatMessage chatMessage) {
    simpMessagingTemplate.convertAndSend(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/private-message")
   public ChatMessage receiveMessage(@Payload ChatMessage chatMessage){
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiverName(),"private",chatMessage);
        System.out.println(chatMessage);
        return chatMessage;
    }
}
