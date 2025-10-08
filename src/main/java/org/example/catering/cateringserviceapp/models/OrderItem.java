package org.example.catering.cateringserviceapp.models;


import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private int quantity;

    private double unitPrice;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }


    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
