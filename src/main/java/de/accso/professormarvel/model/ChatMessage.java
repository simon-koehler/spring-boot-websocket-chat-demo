package de.accso.professormarvel.model;

import org.springframework.data.annotation.Id;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
public class ChatMessage {

    @Id
    protected String id;

    protected MessageType type;
    protected String key;
    protected String ownerKey;
    protected String content;
    protected String sender;
    protected long timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public String getId() {return id;}

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(String ownerKey) {
        this.ownerKey = ownerKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", ownerKey='" + ownerKey + '\'' +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
