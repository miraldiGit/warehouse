package com.miraldi.warehouse.repositories.specifications;

import com.miraldi.warehouse.entities.InventoryItem;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static com.miraldi.warehouse.repositories.specifications.SpecificationUtils.checkNull;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUtils.getLikeOperatorJollyStartEndToLowerCase;

public class SpecificationInventoryItem {

    public SpecificationInventoryItem() {
    }

    public static Specification<InventoryItem> hasItemNameLike(String itemName) {
        if (checkNull(itemName)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("itemName")), getLikeOperatorJollyStartEndToLowerCase(itemName)
                );
    }

    public static Specification<InventoryItem> hasUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("unitPrice").as(String.class), unitPrice.toString());
    }

    public static Specification<InventoryItem> hasQuantity(Integer quantity) {
        if (quantity == null) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("quantity").as(String.class),
                        quantity.toString());
    }

    private static Specification<InventoryItem> noOperator() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
