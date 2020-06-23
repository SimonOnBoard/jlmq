package com.itis.javalab.jlmq.config;


import com.itis.javalab.jlmq.handlers.MyMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    private MyMessageHandler messageHandler;

    public WebSocketConfig(MyMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageHandler, "/queue");
    }
}
