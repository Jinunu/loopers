package com.loopers.application.product;

import com.loopers.domain.product.ProductSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record ProductPageQuery(
        Long loginId,
        Pageable pageable
        ){

    public static ProductPageQuery of(Long loginId, Sort sort, int page, int size) {
        if (page < 0 || size < 0) {
            throw new IllegalArgumentException("페이지 번호와 크기는 0 이상이어야 합니다.");
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return new ProductPageQuery(loginId, pageable);
    }
    public static ProductPageQuery of(Long loginId) {
        int defaultPage = 0;
        int defaultSize = 10;
        Sort defaultSort = Sort.by(Sort.Direction.DESC, ProductSort.SortField.LATEST.getValue());
        Pageable pageable = PageRequest.of(defaultPage, defaultSize, defaultSort);
        new ProductPageQuery(loginId, pageable);
        return new ProductPageQuery(loginId, pageable);
    }

}
