package com.example.convenience_store.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Store {
    @Id
    private Integer store_id;
    private String name;

    @OneToMany(mappedBy = "store")
    private List<Product> products;

    @OneToMany(mappedBy = "store")
    private List<Reservation> reservations;

}
