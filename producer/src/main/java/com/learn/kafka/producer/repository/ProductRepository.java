package com.learn.kafka.producer.repository;

import com.learn.kafka.producer.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
}
