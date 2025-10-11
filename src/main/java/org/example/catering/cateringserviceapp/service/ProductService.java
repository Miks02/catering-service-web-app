package org.example.catering.cateringserviceapp.service;

import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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
            AppLogger.info("Ažuriranje proizvoda ");
            newProduct = productRepository.findProductById(product.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Traženi proizvod " + product.getId() + " ne postoji"));
        }
        else {
            AppLogger.info("Dodavanje proizvoda");
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
        newProduct.setServings(product.getServings());
        newProduct.setDescription(product.getDescription());
        newProduct.setActive(product.isActive());
        newProduct.setProductType(product.getProductType());
        newProduct.setEventType(product.getEventType());

        productRepository.save(newProduct);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getFilteredProducts(Pageable pageable){
        return productRepository.findByActiveTrueAndQuantityGreaterThan(0, pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {return productRepository.findAll(); }


    public void deleteProduct(Long id) {
        productRepository.deleteProductById(id);
    }


}
