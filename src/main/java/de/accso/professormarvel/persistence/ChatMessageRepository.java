package de.accso.professormarvel.persistence;

import de.accso.professormarvel.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> getByKey(String key);
}