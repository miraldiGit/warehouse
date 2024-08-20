package com.miraldi.warehouse.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_name", nullable = false)
    @NotNull
    private String itemName;

    @Column(name = "quantity", nullable = false)
    @NotNull
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    @NotNull
    private BigDecimal unitPrice;

    @ManyToMany(mappedBy = "items")
    private Set<Order> orders = new HashSet<>();
}
