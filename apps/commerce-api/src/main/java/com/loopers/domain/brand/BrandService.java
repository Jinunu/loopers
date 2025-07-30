package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public Brand getBrand(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재 하지 않는 브랜드 입니다."));
    }

    public Brand getBrandByProductId(Long productId) {
        return brandRepository.findByProductsId(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 상품의 브랜드를 찾을 수 없습니다."));
    }

    public List<Brand> getBrandsByProductIds(List<Long> productIds) {
        return brandRepository.findAllByProductIds(productIds);
    }
}
