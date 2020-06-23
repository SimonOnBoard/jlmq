package com.itis.javalab.jlmq.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.jlmq.models.Message;
import com.itis.javalab.jlmq.models.Queue;
import com.itis.javalab.jlmq.models.Status;
import com.itis.javalab.jlmq.repositories.MessageRepository;
import com.itis.javalab.jlmq.repositories.QueueRepository;
import com.itis.javalab.jlmq.services.interfaces.ConsumerService;
import com.itis.javalab.jlmq.services.interfaces.MessageService;
import com.itis.javalab.jlmq.services.interfaces.SimpleQueueAvailabilityService;
import com.itis.javalab.jlmq.services.interfaces.TokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;
    private QueueRepository queueRepository;
    private TokenGenerator tokenGenerator;
    private ConsumerService consumerServices;
    private SimpleQueueAvailabilityService availabilityService;
    private ObjectMapper objectMapper;

    public MessageServiceImpl(MessageRepository messageRepository, QueueRepository queueRepository, TokenGenerator tokenGenerator, ConsumerService consumerServices, SimpleQueueAvailabilityService availabilityService, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.queueRepository = queueRepository;
        this.tokenGenerator = tokenGenerator;
        this.consumerServices = consumerServices;
        this.availabilityService = availabilityService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveAndSend(JsonNode data) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(data.get("payload"));
        String queue = data.get("queue").asText();
        Optional<Queue> queueCandidate = queueRepository.findByName(queue);
        Queue q = queueCandidate.orElse(null);
        Assert.notNull(q, "can't be null");
        Message message = Message.builder()
                .id(tokenGenerator.getToken())
                .queue(q)
                .payload(payload)
                .start(LocalDateTime.now())
                .status(Status.ASSIGNED)
                .build();
        messageRepository.save(message);
        consumerServices.findAndSend(q, message);
    }

    @Override
    public void updateStatus(Status status, String messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setStatus(status);
            messageRepository.save(message);
        });
    }

    @Override
    public void acknowledged(String messageId) {
        this.updateStatus(Status.ACKNOWLEDGED, messageId);
    }

    @Override
    public void completed(String messageId, String queue) {
        this.updateStatus(Status.COMPLETED, messageId);
        availabilityService.makeFree(queue);
    }

    @Override
    public void findMessageFor(String queue, WebSocketSession session) {
        Assert.notNull(queue, "should never be null");
        Queue q = queueRepository.findByName(queue).orElseThrow(IllegalArgumentException::new);
        if (!availabilityService.checkSessionAvailable(q.getName())) return;
        Optional<Message> messageCandidate = messageRepository.findFirstByQueueAndStatusInOrderByStartAsc(q, EnumSet.of(Status.ACKNOWLEDGED, Status.ASSIGNED));
        messageCandidate.ifPresent(message -> {
            consumerServices.send(session, message);
            availabilityService.makeBusy(q.getName());
        });
    }
}
