package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.order.dto.ProductListResponseDto;
import kr.hhplus.be.server.product.controller.swagger.ProductSwaggerDocs;
import kr.hhplus.be.server.product.dto.ProductDetailResponseDto;
import kr.hhplus.be.server.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController implements ProductSwaggerDocs {

    private final ProductService productService;

    // 전체 상품 조회 로직
    @GetMapping
    public ApiResponse<List<ProductListResponseDto>> getAllProducts() {
        List<ProductListResponseDto> products = productService.getAllProducts();

        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, products);
    }

    // 상품 상세 조회 로직
    @GetMapping("/{productId}")
    public ApiResponse<ProductDetailResponseDto> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponseDto product = productService.getProductDetail(productId);

        return ApiResponse.success(ApiResponseCode.OK, product);
    }


}