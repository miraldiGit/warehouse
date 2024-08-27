package com.miraldi.warehouse.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLRestriction("deleted=false")
@Table(name="t_inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_name", unique = true, nullable = false)
    @NotNull
    private String itemName;

    @Column(name = "quantity", nullable = false)
    @NotNull
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal unitPrice;

    @Column(name="deleted")
    private boolean deleted;

    @OneToMany(mappedBy = "inventoryItem")
    private Set<OrderItem> orderItems = new HashSet<>();


    public void decreaseQuantity(Integer quantity){
        this.quantity = this.quantity - quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryItem that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemName);
    }
}
