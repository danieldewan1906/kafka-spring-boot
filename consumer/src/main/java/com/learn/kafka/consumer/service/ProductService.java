package com.learn.kafka.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.consumer.model.Products;
import com.learn.kafka.consumer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(topics = "orders", groupId = "inventory")
    public void productConsumer(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(message, Map.class);
        String orderNo = (String) data.get("orderNo");
        Integer productId = (Integer) data.get("productId");
        Integer qty = (Integer) data.get("quantity");

        System.out.println("Received Order No : " + orderNo);

        Products products = productRepository.findById(productId.longValue()).orElseThrow();
        if (products.getStock() < qty) {
            throw new RuntimeException();
        }

        products.setStock(products.getStock() - qty);
        products.setUpdatedDate(new Date());
        productRepository.save(products);
    }
}
