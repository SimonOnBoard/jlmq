package com.itis.javalab.jlmq.services.implementations;

import com.itis.javalab.jlmq.services.interfaces.TokenGenerator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TokenGeneratorImpl implements TokenGenerator {
    private SecureRandom secureRandom;

    public TokenGeneratorImpl() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String getToken() {
        long longToken = Math.abs(secureRandom.nextLong());
        return (Long.toString(longToken, 16));
    }
}
