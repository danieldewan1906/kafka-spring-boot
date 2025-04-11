package com.learn.kafka.producer.controller;

import com.learn.kafka.producer.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/send")
    public ResponseEntity<?> payment(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = paymentService.payment(request);
        return ResponseEntity.ok(result);
    }
}
