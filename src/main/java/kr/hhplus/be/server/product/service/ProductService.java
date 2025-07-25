package kr.hhplus.be.server.product.service;


import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.dto.ProductDetailResponseDto;
import kr.hhplus.be.server.order.dto.ProductListResponseDto;
import kr.hhplus.be.server.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 전체 상품 목록 조회
    @Transactional(readOnly = true)
    public List<ProductListResponseDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(ProductListResponseDto::from)
                .toList();
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponseDto getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));

        return ProductDetailResponseDto.from(product);
    }


}