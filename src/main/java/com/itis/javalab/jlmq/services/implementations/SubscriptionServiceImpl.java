package com.itis.javalab.jlmq.services.implementations;

import com.itis.javalab.jlmq.SubscriptionException;
import com.itis.javalab.jlmq.models.Queue;
import com.itis.javalab.jlmq.repositories.QueueRepository;
import com.itis.javalab.jlmq.services.interfaces.SimpleQueueAvailabilityService;
import com.itis.javalab.jlmq.services.interfaces.SubscriptionService;
import lombok.Synchronized;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final Map<Queue, WebSocketSession> sessionMap = new HashMap<>();
    private QueueRepository queueRepository;
    private SimpleQueueAvailabilityService queueAvailabilityService;

    public SubscriptionServiceImpl(QueueRepository queueRepository, SimpleQueueAvailabilityService queueAvailabilityService) {
        this.queueRepository = queueRepository;
        this.queueAvailabilityService = queueAvailabilityService;
    }

    @Override
    @Synchronized
    public void subscribe(WebSocketSession session, String queue) throws SubscriptionException {
        Optional<Queue> queueCandidate = queueRepository.findByName(queue);
        Queue q = queueCandidate.orElseThrow(() -> new SubscriptionException("Not a queue", queue));
        if (sessionMap.get(q) != null) throw new SubscriptionException("Already have consumer", q.getName());
        sessionMap.put(q, session);
        queueAvailabilityService.addQueue(q.getName());
    }

    @Override
    public WebSocketSession find(Queue queue) {
        return sessionMap.get(queue);
    }

    @Override
    public void remove(WebSocketSession session) {
        sessionMap.entrySet()
                .removeIf(
                        entry -> (session
                                .equals(entry.getValue())));
    }
}
