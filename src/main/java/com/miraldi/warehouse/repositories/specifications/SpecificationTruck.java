package com.miraldi.warehouse.repositories.specifications;

import com.miraldi.warehouse.entities.Truck;
import org.springframework.data.jpa.domain.Specification;

import static com.miraldi.warehouse.repositories.specifications.SpecificationUtils.checkNull;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUtils.getLikeOperatorJollyStartEndToLowerCase;

public class SpecificationTruck {

    private SpecificationTruck(){
    }

    public static Specification<Truck> hasChassisNumberLike(String chassisNumber) {
        if (checkNull(chassisNumber)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("chassisNumber")), getLikeOperatorJollyStartEndToLowerCase(chassisNumber)
                );
    }

    public static Specification<Truck> hasLicensePlateLike(String licensePlate) {
        if (checkNull(licensePlate)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("licensePlate")), getLikeOperatorJollyStartEndToLowerCase(licensePlate)
                );
    }

    public static Specification<Truck> hasItemsQuantityInTruck(Integer itemsQuantityInTruck) {
        var convertedNumber = String.valueOf(itemsQuantityInTruck);
        if (checkNull(convertedNumber)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("itemsQuantityInTruck").as(String.class),
                        itemsQuantityInTruck.toString());
    }

    public static Specification<Truck> hasDelivered(Boolean delivered) {
        if (delivered == null) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("delivered"), delivered);
    }

    private static Specification<Truck> noOperator() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
