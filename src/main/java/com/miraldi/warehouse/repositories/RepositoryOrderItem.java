package com.miraldi.warehouse.repositories;

import com.miraldi.warehouse.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryOrderItem extends JpaRepository<OrderItem, Long> {

    @Modifying
    @Query(value = "DELETE FROM t_order_item WHERE order_number = :orderNumber", nativeQuery = true)
    void deleteAllByOrder(Long orderNumber);
}
