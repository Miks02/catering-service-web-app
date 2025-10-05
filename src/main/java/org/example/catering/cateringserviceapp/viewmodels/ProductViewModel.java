package org.example.catering.cateringserviceapp.viewmodels;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.example.catering.cateringserviceapp.enums.ProductType;
import org.springframework.web.multipart.MultipartFile;

public class ProductViewModel {
    private Long id;

    @NotBlank(message = "Unesite naziv proizvoda")
    private String name;

    @NotBlank(message = "Unesite opis proizvoda")
    private String description;

    @NotNull(message = "Unesite sliku proizvoda")
    private MultipartFile imageFile;

    private String imagePath;

    @Positive(message = "Cena mora biti veća od 0 RSD")
    private double price;

    @NotBlank(message = "Unesite tip proizvoda")
    private String productType;

    @NotBlank(message = "Unesite tip proslave")
    private String eventType;

    @Min(value = 1, message = "Najmanji broj porcija je 1")
    private int servings = 1;

    @Min(value = 0, message = "Količina ne može biti manja od 0")
    private int quantity = 0;

    private boolean active = false;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
