package com.itis.javalab.jlmq.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.itis.javalab.jlmq.models.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDto {
    private String command;
    private JsonNode payload;
    private String objectId;
    private String queue;
    public static MessageDto from(String queue, String objectId, String command, JsonNode payload) {
        return MessageDto.builder()
                .queue(queue)
                .command(command)
                .payload(payload)
                .objectId(objectId)
                .build();
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "command='" + command + '\'' +
                ", payload=" + payload +
                ", objectId='" + objectId + '\'' +
                ", queue='" + queue + '\'' +
                '}';
    }
}
