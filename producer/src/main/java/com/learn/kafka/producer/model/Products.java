package com.learn.kafka.producer.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(schema = "ecommerce", name = "products")
public class Products implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_gen")
    @SequenceGenerator(name = "products_gen", sequenceName = "products_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "stocks", nullable = false)
    private Long stock;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate = new Date();

    @Column(name = "updated_date")
    private Date updatedDate;
}
