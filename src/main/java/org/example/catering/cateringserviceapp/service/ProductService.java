package org.example.catering.cateringserviceapp.service;

import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product, MultipartFile productImage) throws IOException, IllegalArgumentException {

        Product newProduct;

        if(product.getId() != null){
           newProduct = productRepository.findProductById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Traženi proizvod " + product.getId() + " ne postoji"));
        }
        else {
            newProduct = new Product();
        }

        Date createdAt = new Date(System.currentTimeMillis());
        if(productImage != null && !productImage.isEmpty()) {
            String storageFileName = createdAt.getTime() + "_" + productImage.getOriginalFilename();

            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = productImage.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImagePath(storageFileName);
            }
        }


        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setQuantity(product.getQuantity());
        newProduct.setDescription(product.getDescription());
        newProduct.setActive(product.isActive());
        newProduct.setProductType(product.getProductType());
        newProduct.setEventType(product.getEventType());

        productRepository.save(newProduct);
    }

    public Optional<List<Product>> findAll() {
        return Optional.of(productRepository.findAll());
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findProductByName(name);
    }


}
