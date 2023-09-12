package com.example.convenience_store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    private Integer productId;
    private String name;
    private int quantity;
    private int price;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
