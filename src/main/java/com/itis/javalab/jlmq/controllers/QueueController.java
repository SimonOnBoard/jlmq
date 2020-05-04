package com.itis.javalab.jlmq.controllers;

import com.itis.javalab.jlmq.services.interfaces.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QueueController {
    private QueueService queueService;

    public QueueController(QueueService queueService) {
        System.out.println("Created");
        this.queueService = queueService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createQueue(@RequestParam("name") String name) {
        int code = queueService.saveQueue(name);
        switch (code) {
            case 200:
                return ResponseEntity.ok("All is ok");
            case 208:
                return ResponseEntity.badRequest().body("Already exists");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/create")
    public String getCreatorPage(Model model) {
        return "start";
    }
}
