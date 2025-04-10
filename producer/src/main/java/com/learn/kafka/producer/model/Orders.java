package com.learn.kafka.producer.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(schema = "ecommerce", name = "orders")
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_gen")
    @SequenceGenerator(name = "orders_gen", sequenceName = "orders_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true)
    private String orderNo;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate = new Date();

    @Column(name = "updated_date")
    private Date updatedDate;

}
