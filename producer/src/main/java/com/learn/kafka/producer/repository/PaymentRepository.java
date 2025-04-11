package com.learn.kafka.producer.repository;

import com.learn.kafka.producer.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {

    Payments findByOrderNo(String orderNo);
}
