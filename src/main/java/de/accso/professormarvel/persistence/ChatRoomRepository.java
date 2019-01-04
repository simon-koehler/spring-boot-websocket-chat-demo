package de.accso.professormarvel.persistence;

import de.accso.professormarvel.model.ChatMessage;
import de.accso.professormarvel.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> getByKey(String key);
}