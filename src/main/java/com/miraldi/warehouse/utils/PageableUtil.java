package com.miraldi.warehouse.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtil {

    public Pageable getPageable(Pageable pageable, Sort defaultSort) {
        Sort sort = pageable.getSort();

        boolean sortInPageableIsEmpty = pageable.getSort().isEmpty();
        boolean defaultSortIsNotEmpty = !defaultSort.isEmpty();

        if (sortInPageableIsEmpty && defaultSortIsNotEmpty) {
            sort = defaultSort;
        }

        Pageable pageableForSelect = null;
        if (pageable.isUnpaged()) {
            pageableForSelect = Pageable.unpaged(sort);
        } else {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();

            pageableForSelect = PageRequest.of(pageNumber, pageSize, sort);
        }

        return pageableForSelect;
    }
}
