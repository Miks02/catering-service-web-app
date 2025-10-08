package org.example.catering.cateringserviceapp.repository;

import org.example.catering.cateringserviceapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
