package com.itis.javalab.jlmq.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.jlmq.dto.MessageDto;
import com.itis.javalab.jlmq.models.Message;
import com.itis.javalab.jlmq.models.Queue;
import com.itis.javalab.jlmq.services.interfaces.ConsumerService;
import com.itis.javalab.jlmq.services.interfaces.SimpleQueueAvailabilityService;
import com.itis.javalab.jlmq.services.interfaces.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    private ObjectMapper objectMapper;
    private SimpleQueueAvailabilityService availabilityService;
    private SubscriptionService subscriptionService;

    public ConsumerServiceImpl(ObjectMapper objectMapper, SimpleQueueAvailabilityService availabilityService, SubscriptionService subscriptionService) {
        this.objectMapper = objectMapper;
        this.availabilityService = availabilityService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void findAndSend(Queue queue, Message message) {
        WebSocketSession webSocketSession = subscriptionService.find(queue);
        if (availabilityService.checkSessionAvailable(queue.getName()) && checkSession(webSocketSession)) {
            send(webSocketSession, message);
            availabilityService.makeBusy(queue.getName());
        }
    }

    @Override
    public void send(WebSocketSession session, Message message) {
        boolean checkSession = checkSession(session);
        if (checkSession) {
            try {
                session.sendMessage(getTextMessage(message));
            } catch (IOException e) {
                System.err.println(e.getMessage());;
                System.out.println("Message sending failed because session is closed");
            }
        }
    }


    private WebSocketMessage<?> getTextMessage(Message message) {
        try {
            MessageDto messageDto = MessageDto.from(message.getQueue().getName(), message.getId(), "do", objectMapper.readTree(message.getPayload()));
            return new TextMessage(objectMapper.writeValueAsString(messageDto));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean checkSession(WebSocketSession webSocketSession) {
        if (webSocketSession == null) return false;
        return webSocketSession.isOpen();
    }
}
