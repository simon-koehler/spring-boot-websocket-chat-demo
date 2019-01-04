package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat.sendMessage-{key}")
    @SendTo("/topic/room-{key}")
    public ChatMessage sendMessage(
            @DestinationVariable String key,
            @Payload ChatMessage chatMessage) {
        logger.info(chatMessage.getSender() + " said \"" +  chatMessage.getContent() + "\"");
        return chatMessage;
    }

    @MessageMapping("/chat.addUser-{key}")
    @SendTo("/topic/room-{key}")
    public ChatMessage addUser(@DestinationVariable String key,
                               @Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        logger.info("received addUser: " + chatMessage);
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
