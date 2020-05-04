package com.itis.javalab.jlmq.services.interfaces;

import com.itis.javalab.jlmq.models.Queue;

public interface SimpleQueueAvailabilityService {
    boolean checkSessionAvailable(String queue);
    void makeFree(String queue);
    void makeBusy(String queue);
    void addQueue(String queue);
}
