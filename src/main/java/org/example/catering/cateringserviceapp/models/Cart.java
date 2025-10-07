package org.example.catering.cateringserviceapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String userId;



}
