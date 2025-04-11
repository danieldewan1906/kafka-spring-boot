package com.learn.kafka.producer.repository;

import com.learn.kafka.producer.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    Orders findByOrderNo(String orderNo);
}
