package kr.hhplus.be.server.mock.controller;


import kr.hhplus.be.server.mock.controller.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockProductDto.PopularProductDto;
import kr.hhplus.be.server.mock.controller.dto.MockProductDto.ProductResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mock/products")
public class ProductController implements ProductControllerSpec{

    /** 제품 전체조회 */
    @GetMapping
    public CommonResponse<List<ProductResponseDto>> getAllProducts() {
        return CommonResponse.success("성공", List.of(
                new ProductResponseDto(1L, "상품1", 10000, 50),
                new ProductResponseDto(2L, "상품2", 8000, 10)
        ), 200);
    }

    /** 제품 상세조회*/
    @GetMapping("/{productId}")
    public CommonResponse<ProductResponseDto> getProductDetail(@PathVariable Long productId) {
        return CommonResponse.success("성공", new ProductResponseDto(productId, "상품1", 10000, 50), 200);
    }

    /** 인기상품조회 */
    @GetMapping("/popular")
    public CommonResponse<List<PopularProductDto>> getPopularProducts(
            @RequestParam int days,
            @RequestParam int limit
    ) {
        return CommonResponse.success("성공", List.of(
                new PopularProductDto(1L, 120),
                new PopularProductDto(2L, 90)
        ), 200);
    }
}
