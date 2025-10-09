package org.example.catering.cateringserviceapp.repository;

import org.example.catering.cateringserviceapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
