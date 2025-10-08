package org.example.catering.cateringserviceapp.models;

import jakarta.persistence.*;
import org.example.catering.cateringserviceapp.enums.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    private LocalDateTime orderDate;

    private String shippingAddress;

    private double orderPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public double getOrderPrice() { return orderPrice; }
    public void setOrderPrice(double orderPrice) { this.orderPrice = orderPrice; }

    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) { this.status = status; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}