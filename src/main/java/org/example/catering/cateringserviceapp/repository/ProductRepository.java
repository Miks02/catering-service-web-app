package org.example.catering.cateringserviceapp.repository;

import jakarta.transaction.Transactional;
import org.example.catering.cateringserviceapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Optional<Product> findProductById(Long id);
    public Optional<Product> findProductByName(String name);
    Page<Product> findByActiveTrueAndQuantityGreaterThan(int quantity, Pageable pageable);
    @Transactional
    public void deleteProductById(Long id);

}
