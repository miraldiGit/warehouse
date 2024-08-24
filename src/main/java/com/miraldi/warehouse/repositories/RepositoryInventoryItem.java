package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryInventoryItem extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findById(Long id);

    Page<InventoryItem> findAll(Specification<InventoryItem> spec, Pageable pageable);

    boolean existsByItemName(String itemName);
}
