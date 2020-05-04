package com.itis.javalab.jlmq.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Message {
    @Id
    @Column(name = "id", unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String payload;

    private LocalDateTime start;

}
