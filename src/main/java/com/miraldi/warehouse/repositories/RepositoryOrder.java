package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryOrder extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {
            "orderItems",
            "user"
    })
    Optional<Order> findByOrderNumber(Long orderNumber);

    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

}
