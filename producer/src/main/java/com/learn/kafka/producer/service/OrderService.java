package com.learn.kafka.producer.service;

import java.util.Map;

public interface OrderService {
    Map<String, Object> checkout(Map<String, Object> data);
}
