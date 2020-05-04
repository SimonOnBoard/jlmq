package com.itis.javalab.jlmq.repositories;

import com.itis.javalab.jlmq.models.Message;
import com.itis.javalab.jlmq.models.Queue;
import com.itis.javalab.jlmq.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Optional<Message> findFirstByQueueAndStatusInOrderByStartAsc(Queue queue, Set<Status> statusSet);
}
