package com.miraldi.warehouse.repositories.specifications;

import io.micrometer.common.util.StringUtils;

public class SpecificationUtils {

    private SpecificationUtils() {
    }

    private static final String JOLLY = "%";

    public static String getLikeOperatorJollyStartEndToLowerCase(String likeField) {
        var likeOperator = JOLLY + likeField + JOLLY;
        return likeOperator.toLowerCase();
    }

    public static boolean checkNull(String value) {
        return StringUtils.isBlank(value);
    }
}
