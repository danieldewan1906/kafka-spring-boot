package com.learn.kafka.producer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.producer.model.Orders;
import com.learn.kafka.producer.model.Payments;
import com.learn.kafka.producer.repository.OrderRepository;
import com.learn.kafka.producer.repository.PaymentRepository;
import com.learn.kafka.producer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Map<String, Object> checkout(Map<String, Object> data) {
        Integer productId = (Integer) data.get("productId");
        Integer qty = (Integer) data.get("quantity");
        Integer total = (Integer) data.get("total");
        String orderNo = generateOrderNo();

        Orders orders = new Orders();
        orders.setStatus("CREATED");
        orders.setProductId(productId.longValue());
        orders.setQuantity(qty);
        orders.setTotal(new BigDecimal(total));
        orders.setCreatedDate(new Date());
        orders.setOrderNo(orderNo);
        orderRepository.save(orders);

        Payments payments = new Payments();
        payments.setOrderNo(orderNo);
        payments.setStatus("PAYMENT_PENDING");
        payments.setCreatedDate(new Date());
        paymentRepository.save(payments);

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("message", "Order Created");
        response.put("data", data);
        return response;
    }

    private String generateOrderNo() {
        String number = "0123456789";
        Random random = new Random();
        StringBuilder strings = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            strings.append(number.charAt(random.nextInt(number.length())));
        }
        return "KAF" + strings;
    }
}
