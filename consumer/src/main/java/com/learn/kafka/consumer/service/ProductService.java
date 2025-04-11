package com.learn.kafka.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.consumer.model.Products;
import com.learn.kafka.consumer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "orders", groupId = "inventory")
    public void productConsumer(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(message, Map.class);
        String orderNo = (String) data.get("orderNo");
        Integer productId = (Integer) data.get("productId");
        Integer qty = (Integer) data.get("quantity");
        String paymentMethod = (String) data.get("paymentMethod");

        System.out.println("Received Order No : " + orderNo);

        Products products = productRepository.findById(productId.longValue()).orElseThrow();
        try {
            Map<String, Object> callback = new HashMap<>();
            if (products.getStock() < qty || products.getId() == null || !products.getIsActive()) {
                callback.put("orderNo", orderNo);
                callback.put("status", "PAYMENT_PENDING");
                callback.put("paymentMethod", "-");
                callback.put("reason", "Stock is not enough or Product not active");
            } else {
                callback.put("orderNo", orderNo);
                callback.put("status", "PAYMENT_COMPLETED");
                callback.put("paymentMethod", paymentMethod);
                callback.put("reason", "-");
                products.setStock(products.getStock() - qty);
                products.setUpdatedDate(new Date());
                productRepository.save(products);
            }
            String value = mapper.writeValueAsString(callback);
            kafkaTemplate.send("inventory-callback", value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
