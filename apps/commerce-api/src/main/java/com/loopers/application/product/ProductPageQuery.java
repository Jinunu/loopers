package com.loopers.application.product;

import com.loopers.domain.product.ProductSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record ProductPageQuery(
        Long loginId,
        Pageable pageable,
        Long brandId
        ){

    public static ProductPageQuery of(Long loginId, Sort sort, int page, int size) {

        if (page < 0 || size < 0) {
            throw new IllegalArgumentException("페이지 번호와 크기는 0 이상이어야 합니다.");
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return new ProductPageQuery(loginId, pageable, null);
    }
    public static ProductPageQuery of(Long loginId) {
        int defaultPage = 0;
        int defaultSize = 10;
        Sort defaultSort = Sort.by(Sort.Direction.DESC, ProductSort.SortField.LATEST.getValue());
        Pageable pageable = PageRequest.of(defaultPage, defaultSize, defaultSort);
        return new ProductPageQuery(loginId, pageable, null);
    }

    // 신규 팩토리: 문자열 기반 → ProductSort → Spring Sort 변환
    public static ProductPageQuery of(Long loginId, String sortField, String sortDirection, int page, int size) {
        ProductSort.SortField field = ProductSort.SortField.fromString(sortField);
        ProductSort.SortDirection direction = ProductSort.SortDirection.fromString(sortDirection);

        Sort.Direction dir = (direction == ProductSort.SortDirection.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir , field.getValue()));
        return new ProductPageQuery(loginId, pageable, null);
    }

    public static ProductPageQuery of(Long loginId, String sortField, String sortDirection, int page, int size, Long brandId) {
        ProductSort.SortField field = ProductSort.SortField.fromString(sortField);
        ProductSort.SortDirection direction = ProductSort.SortDirection.fromString(sortDirection);
        Sort.Direction dir = (direction == ProductSort.SortDirection.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir , field.getValue()));
        return new ProductPageQuery(loginId, pageable, brandId);
    }




}
