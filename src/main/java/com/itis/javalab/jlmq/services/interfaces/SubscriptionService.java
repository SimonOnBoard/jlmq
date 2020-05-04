package com.itis.javalab.jlmq.services.interfaces;

import com.itis.javalab.jlmq.SubscriptionException;
import com.itis.javalab.jlmq.models.Queue;
import org.springframework.web.socket.WebSocketSession;

public interface SubscriptionService {
    void subscribe(WebSocketSession session, String queue) throws SubscriptionException;
    WebSocketSession find(Queue queue);
    void remove(WebSocketSession session);
}
