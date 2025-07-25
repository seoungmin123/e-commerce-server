package kr.hhplus.be.server.product.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.order.dto.ProductListResponseDto;
import kr.hhplus.be.server.product.dto.ProductDetailResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Product API", description = "상품 정보 조회 API")
public interface ProductSwaggerDocs {

    @Operation(summary = "전체 상품 목록 조회", description = "모든 상품의 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 리스트 조회 성공")
    })
    @GetMapping("/products")
    kr.hhplus.be.server.common.response.ApiResponse<List<ProductListResponseDto>> getAllProducts();

    @Operation(summary = "상품 상세 조회", description = "특정 productId로 상품 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공")
    })
    @GetMapping("/products/{productId}")
    kr.hhplus.be.server.common.response.ApiResponse<ProductDetailResponseDto> getProductDetail(@PathVariable Long productId);
}