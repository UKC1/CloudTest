package com.example.convenience_store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    private Integer product_id;
    private String name;
    private int quantity;
    private int price;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
