package de.accso.professormarvel.controller;

import de.accso.professormarvel.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for joining rooms
 */
public class JoinMessage extends ChatMessage {

    private List<ChatMessage> recentMessages = new ArrayList<>();

    public JoinMessage(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.key = chatMessage.getKey();
        this.content = chatMessage.getContent();
        this.ownerKey = chatMessage.getOwnerKey();
        this.sender = chatMessage.getSender();
        this.type = chatMessage.getType();
        this.timestamp = chatMessage.getTimestamp();
    }

    public List<ChatMessage> getRecentMessages() {
        return recentMessages;
    }

    public void setRecentMessages(List<ChatMessage> recentMessages) {
        this.recentMessages = recentMessages;
    }
}
