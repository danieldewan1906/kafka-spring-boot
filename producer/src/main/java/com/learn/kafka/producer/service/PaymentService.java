package com.learn.kafka.producer.service;

import java.util.Map;

public interface PaymentService {
    Map<String, Object> payment(Map<String, Object> request);
}
