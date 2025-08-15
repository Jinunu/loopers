package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "상품 V1 API", description = "상품 API")
public interface ProductV1ApiSpec {

    @Operation(summary = "상품 상세 조회", description = "상품ID로 상품 상세 정보를 조회")
    ApiResponse<ProductV1Dto.ProductDetailResponse> getProduct(
            @Parameter(description = "상품 ID") Long productId,
            @Parameter(description = "요청 사용자 ID (헤더)") Long userId
    );

    @Operation(summary = "상품 목록 조회", description = "페이지 및 정렬 조건으로 상품 목록 조회")
    ApiResponse<PageResponse<ProductV1Dto.ProductListItem>> getProducts(
            @Parameter(description = "페이지 번호(0 기반)") Integer page,
            @Parameter(description = "페이지 크기") Integer size,
            @Parameter(description = "정렬 필드(price, createdAt, likes)") String sortField,
            @Parameter(description = "정렬 방향(asc, desc)") String sortDirection,
            @Parameter(description = "브랜드 ID 필터") Long brandId,
            @Parameter(description = "요청 사용자 ID (헤더)") Long userId
    );
}
