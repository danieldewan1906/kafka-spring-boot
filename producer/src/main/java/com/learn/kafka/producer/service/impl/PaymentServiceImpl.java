package com.learn.kafka.producer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.producer.model.Orders;
import com.learn.kafka.producer.model.Payments;
import com.learn.kafka.producer.repository.OrderRepository;
import com.learn.kafka.producer.repository.PaymentRepository;
import com.learn.kafka.producer.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String TOPIC = "orders";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Map<String, Object> payment(Map<String, Object> request) {
        String orderNo = (String) request.get("orderNo");
        String paymentMethod = (String) request.get("paymentMethod");
        ObjectMapper mapper = new ObjectMapper();

        Orders orders = orderRepository.findByOrderNo(orderNo);
        if (orders == null) {
            throw new RuntimeException();
        }

        Payments payments = paymentRepository.findByOrderNo(orderNo);
        if (payments == null) {
            throw new RuntimeException();
        }

        try {
            request.put("productId", orders.getProductId());
            request.put("quantity", orders.getQuantity());
            request.put("total", orders.getTotal());
            request.put("paymentMethod", paymentMethod);
            String value = mapper.writeValueAsString(request);
            kafkaTemplate.send(TOPIC, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = new HashMap<>();
        request.put("paymentStatus", "PAYMENT_PENDING");
        response.put("statusCode", 200);
        response.put("message", "Payment Successfully. Waiting For Stock Confirmation");
        response.put("data", request);
        return response;
    }

    @KafkaListener(topics = "inventory-callback", groupId = "stock-callback")
    public void handleConsumerCallback(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = mapper.readValue(message, Map.class);
        String orderNo = (String) data.get("orderNo");
        String status = (String) data.get("status");
        String reason = (String) data.get("reason");
        String paymentMethod = (String) data.get("paymentMethod");

        System.out.println("Received Callback Order No : " + orderNo);

        Orders orders = orderRepository.findByOrderNo(orderNo);
        Payments payments = paymentRepository.findByOrderNo(orderNo);
        if (status.equals("PAYMENT_COMPLETED") && reason.equals("-")) {
            orders.setStatus("COMPLETED");
            orders.setUpdatedDate(new Date());
            orderRepository.save(orders);

            payments.setPaymentMethod(paymentMethod);
            payments.setStatus("PAYMENT_COMPLETED");
            payments.setUpdatedDate(new Date());
            paymentRepository.save(payments);
        }
    }
}
