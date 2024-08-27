package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RepositoryInventoryItem extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findById(Long id);

    Optional<InventoryItem> findByItemName(String itemName);

    Page<InventoryItem> findAll(Specification<InventoryItem> spec, Pageable pageable);

    Set<InventoryItem> findAllByItemNameIn(List<String> itemNames);

    boolean existsByItemName(String itemName);
}
