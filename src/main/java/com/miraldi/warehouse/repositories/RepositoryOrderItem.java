package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryOrderItem extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findById(Long id);
}
