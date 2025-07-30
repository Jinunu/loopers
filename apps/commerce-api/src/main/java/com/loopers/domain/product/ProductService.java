package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재 하지 않는 상풉입니다."));
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
