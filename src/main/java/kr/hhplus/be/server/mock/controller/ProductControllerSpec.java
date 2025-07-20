package kr.hhplus.be.server.mock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.app.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "상품", description = "상품 관련 API")
public interface ProductControllerSpec {

    @Operation(
            summary = "전체 상품 조회",
            description = "판매 중인 전체 상품 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
            }
    )
    @GetMapping
    CommonResponse<List<MockProductDto.ProductResponseDto>> getAllProducts();

    @Operation(
            summary = "상품 상세 조회",
            description = "특정 상품 ID에 대한 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "상품 없음 예시",
                                            value = """
                        {
                          "success": false,
                          "message": "해당 상품을 찾을 수 없습니다.",
                          "data": null,
                          "status": 404
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{productId}")
    CommonResponse<MockProductDto.ProductResponseDto> getProductDetail(
            @Parameter(description = "상품 ID", required = true)
            @PathVariable Long productId
    );

    @Operation(
            summary = "인기 상품 조회",
            description = "지정한 기간 내 많이 판매된 인기 상품을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 상품 조회 성공")
            }
    )
    @GetMapping("/popular")
    CommonResponse<List<MockProductDto.PopularProductDto>> getPopularProducts(
            @Parameter(description = "조회 대상 기간 (일 단위)", example = "3")
            @RequestParam int days,

            @Parameter(description = "조회할 상품 수", example = "5")
            @RequestParam int limit
    );
}
