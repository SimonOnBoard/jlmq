package com.itis.javalab.jlmq.services.implementations;

import com.itis.javalab.jlmq.models.Queue;
import com.itis.javalab.jlmq.repositories.QueueRepository;
import com.itis.javalab.jlmq.services.interfaces.QueueService;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;


@Service
public class QueueServiceImpl implements QueueService {
    private QueueRepository queueRepository;

    public QueueServiceImpl(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    @Override
    public int saveQueue(String name) {
        return queueRepository.findByName(name).isPresent() ? 208 : save(name);
    }

    private int save(String name) {
        queueRepository.save(Queue.builder().name(name).build());
        return 200;
    }
}
