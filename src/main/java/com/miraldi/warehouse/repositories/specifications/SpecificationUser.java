package com.miraldi.warehouse.repositories.specifications;

import com.miraldi.warehouse.entities.User;
import com.miraldi.warehouse.utils.Role;
import org.springframework.data.jpa.domain.Specification;

import static com.miraldi.warehouse.repositories.specifications.SpecificationUtils.checkNull;
import static com.miraldi.warehouse.repositories.specifications.SpecificationUtils.getLikeOperatorJollyStartEndToLowerCase;

public class SpecificationUser {

    SpecificationUser() {
    }

    public static Specification<User> hasRole(Role role) {
        if (role == null) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role);
    }

    public static Specification<User> hasUsernameLike(String username) {
        if (checkNull(username)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")), getLikeOperatorJollyStartEndToLowerCase(username)
                );
    }

    public static Specification<User> hasLastNameLike(String lastName) {
        if (checkNull(lastName)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")), getLikeOperatorJollyStartEndToLowerCase(lastName)
                );
    }

    public static Specification<User> hasFirstNameLike(String firstName) {
        if (checkNull(firstName)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")), getLikeOperatorJollyStartEndToLowerCase(firstName)
                );
    }

    public static Specification<User> hasEmailLike(String email) {
        if (checkNull(email)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")), getLikeOperatorJollyStartEndToLowerCase(email)
                );
    }

    public static Specification<User> hasCityLike(String city) {
        if (checkNull(city)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city")), getLikeOperatorJollyStartEndToLowerCase(city)
                );
    }

    public static Specification<User> hasCountryLike(String country) {
        if (checkNull(country)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("country")), getLikeOperatorJollyStartEndToLowerCase(country)
                );
    }

    public static Specification<User> hasPostalCode(Integer postalCode) {
        var convertedNumber = String.valueOf(postalCode);
        if (checkNull(convertedNumber)) {
            return noOperator();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("postalCode").as(String.class),
                        postalCode.toString());
    }

    private static Specification<User> noOperator() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
