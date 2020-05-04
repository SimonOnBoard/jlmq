package com.itis.javalab.jlmq.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.jlmq.SubscriptionException;
import com.itis.javalab.jlmq.dto.MessageDto;
import com.itis.javalab.jlmq.models.Status;
import com.itis.javalab.jlmq.services.interfaces.ConsumerService;
import com.itis.javalab.jlmq.services.interfaces.MessageService;
import com.itis.javalab.jlmq.services.interfaces.SimpleQueueAvailabilityService;
import com.itis.javalab.jlmq.services.interfaces.SubscriptionService;
import com.itis.javalab.jlmq.utils.Reader;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.function.Consumer;

@Component("messageHandler")
@EnableWebSocket
public class MyMessageHandler extends TextWebSocketHandler {
    private ObjectMapper objectMapper;
    private MessageService messageService;
    private SubscriptionService subscriptionService;

    public MyMessageHandler(ObjectMapper objectMapper, MessageService messageService, SubscriptionService subscriptionService) {
        this.objectMapper = objectMapper;
        this.messageService = messageService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            String command;
            String messageId;
            String queue;
            String messageText = message.getPayload();
            JsonNode jsonNode = objectMapper.readTree(messageText);
            if (jsonNode.isNull() || jsonNode.isMissingNode() || (command = jsonNode.get("option").asText()) == null) {
                throw new IllegalStateException("Bad data");
            }
            queue = Reader.getAttribute(jsonNode, "queue");
            messageId = Reader.getAttribute(jsonNode, "message");
            switch (command) {
                case "task":
                    messageService.saveAndSend(jsonNode);
                    break;
                case "startProducer":
                    // TODO придумать подписку producer
                    break;
                case "subscribe":
                    subscriptionService.subscribe(session, queue);
                    messageService.findMessageFor(queue, session);
                    break;
                case "acknowledged":
                    messageService.acknowledged(messageId);
                    break;
                case "completed":
                    messageService.completed(messageId, queue);
                    messageService.findMessageFor(queue, session);
                    break;
                case "failed":
                    break;
                default:
                    throw new IllegalStateException("Command not found");
            }
        } catch (SubscriptionException e) {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(
                            MessageDto.from(e.q, null, "drop", null))));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        subscriptionService.remove(session);
        super.afterConnectionClosed(session, status);
    }
}
