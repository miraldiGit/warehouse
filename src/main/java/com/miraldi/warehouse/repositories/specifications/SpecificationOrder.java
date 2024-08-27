package com.miraldi.warehouse.repositories.specifications;

import com.miraldi.warehouse.entities.Order;
import com.miraldi.warehouse.entities.User;
import com.miraldi.warehouse.utils.Status;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationOrder {

    public SpecificationOrder() {
    }

    public static Specification<Order> hasOrderNumberLike(Long orderNumber) {
        if (orderNumber == null) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("orderNumber").as(String.class), orderNumber.toString());
    }

    public static Specification<Order> hasStatus(Status status) {
        if (status == null) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Order> hasUser(User user) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), user);
    }

    private static Specification<Order> noOperator() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
