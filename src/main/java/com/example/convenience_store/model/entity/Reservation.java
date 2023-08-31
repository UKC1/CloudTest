package com.example.convenience_store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer num;
    private int quantity;
    private int price;
    private Timestamp time;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Store store;
}
