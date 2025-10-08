package org.example.catering.cateringserviceapp.repository;

import org.example.catering.cateringserviceapp.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
