package com.itis.javalab.jlmq.services.interfaces;

import com.itis.javalab.jlmq.models.Message;
import com.itis.javalab.jlmq.models.Queue;
import org.springframework.web.socket.WebSocketSession;

public interface ConsumerService {
    void findAndSend(Queue queue, Message message);
    void send(WebSocketSession session, Message message);
}
