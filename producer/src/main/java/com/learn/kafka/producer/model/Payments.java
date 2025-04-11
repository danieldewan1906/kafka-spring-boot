package com.learn.kafka.producer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "ecommerce", name = "payments")
public class Payments implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_gen")
    @SequenceGenerator(name = "payments_gen", sequenceName = "payments_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "order_no", nullable = false)
    private String orderNo;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate = new Date();

    @Column(name = "updated_date")
    private Date updatedDate;
}
