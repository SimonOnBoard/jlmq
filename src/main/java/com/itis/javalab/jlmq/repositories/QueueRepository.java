package com.itis.javalab.jlmq.repositories;

import com.itis.javalab.jlmq.models.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue,Long> {
    Optional<Queue> findByName(String name);
}
