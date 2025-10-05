package org.example.catering.cateringserviceapp.models;

import jakarta.persistence.*;
import org.example.catering.cateringserviceapp.enums.EventType;
import org.example.catering.cateringserviceapp.enums.ProductType;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String imagePath;

    private int timesBought = 0;

    private int quantity = 0;

    private boolean active = false;

}
