package com.itis.javalab.jlmq;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class SubscriptionException extends Exception {
    public String q;
    public SubscriptionException(String message,String q) {
        super(message);
        this.q = q;
    }
}
