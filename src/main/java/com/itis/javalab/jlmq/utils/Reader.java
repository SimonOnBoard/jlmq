package com.itis.javalab.jlmq.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class Reader {
    public static String getAttribute(JsonNode jsonNode, String queue) {
        JsonNode q = jsonNode.get(queue);
        return q != null ? q.asText() : null;
    }
}
