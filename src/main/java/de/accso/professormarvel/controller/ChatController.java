package de.accso.professormarvel.controller;

import de.accso.professormarvel.model.ChatMessage;
import de.accso.professormarvel.model.ChatRoom;
import de.accso.professormarvel.persistence.ChatMessageRepository;
import de.accso.professormarvel.persistence.ChatRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatController(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @MessageMapping("/chat.sendMessage-{key}")
    @SendTo("/topic/room-{key}")
    public ChatMessage sendMessage(
            @DestinationVariable String key,
            @Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(System.currentTimeMillis());
        chatMessage.setKey(key);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser-{key}")
    @SendTo("/topic/room-{key}")
    public JoinMessage addUser(@DestinationVariable String key,
                                     @Payload ChatMessage chatMessage,
                                     SimpMessageHeaderAccessor headerAccessor) {
        chatMessage.setTimestamp(System.currentTimeMillis());

        if (chatRoomRepository.getByKey(key).isEmpty()) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setKey(key);
            chatRoom.setOwnerKey(chatMessage.getOwnerKey());
            chatRoom.setOwner(chatMessage.getSender());
            chatRoomRepository.save(chatRoom);
        }
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        // bundle all recent messages into joinmessage and return
        JoinMessage jm = new JoinMessage(chatMessage);
        jm.getRecentMessages().addAll(chatMessageRepository.getByKey(key));

        // save joining as separate message
        chatMessage.setKey(key);
        chatMessageRepository.save(chatMessage); // save after chatMessagesRepository.getByKey, or you get a message twice

        return jm;
    }

}
