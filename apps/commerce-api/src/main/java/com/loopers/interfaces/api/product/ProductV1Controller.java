package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductPageQuery;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.user.UserRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductV1Controller implements ProductV1ApiSpec {

    private final ProductFacade productFacade;
    private final UserRepository userRepository;

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductV1Dto.ProductDetailResponse> getProduct(@PathVariable Long productId,
                                                                      @RequestHeader(name = "X-USER-ID", required = false) Long userId) {
        ProductInfo info = productFacade.getProductInfo(productId, userId);
        return ApiResponse.success(ProductV1Dto.ProductDetailResponse.from(info));
    }

    @Override
    @GetMapping
    public  ApiResponse<PageResponse<ProductV1Dto.ProductListItem>> getProducts(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortField", required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection,
            @RequestHeader(name = "X-USER-ID", required = false) Long userId
    ) {


        Page<ProductInfo> productInfos = productFacade.getProductInfoList(ProductPageQuery.of(userId, sortField, sortDirection, page, size));

        PageResponse<ProductV1Dto.ProductListItem> from = PageResponse.from(productInfos.map(ProductV1Dto.ProductListItem::from));
        return ApiResponse.success(from);
    }

}

