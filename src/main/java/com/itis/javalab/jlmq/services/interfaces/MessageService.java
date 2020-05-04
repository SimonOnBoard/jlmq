package com.itis.javalab.jlmq.services.interfaces;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.itis.javalab.jlmq.models.Status;
import org.springframework.web.socket.WebSocketSession;

public interface MessageService {

    void findMessageFor(String queue, WebSocketSession session);

    void saveAndSend(JsonNode jsonNode) throws JsonProcessingException;

    void updateStatus(Status acknowledged, String messageId);

    void acknowledged(String messageId);

    void completed(String messageId,String queue);
}
