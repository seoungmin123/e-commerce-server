package kr.hhplus.be.server.mock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.mock.controller.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockOrderDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "주문", description = "주문 및 결제 관련 API")
public interface OrderControllerSpec {

    @Operation(
            summary = "주문 요청",
            description = "사용자가 하나 이상의 상품을 주문합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MockOrderDto.OrderRequestDto.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      "userId": 1,
                      "items": [
                        { "productId": 1, "item_cnt": 2 },
                        { "productId": 2, "item_cnt": 1 }
                      ],
                      "couponId": 5
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 완료"),
                    @ApiResponse(responseCode = "400", description = "상품 재고/포인트/쿠폰 문제",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "포인트 부족",
                                            value = """
                        {
                          "success": false,
                          "message": "보유 포인트가 부족합니다.",
                          "data": null,
                          "status": 400
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "사용자 또는 상품 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "사용자 없음",
                                            value = """
                        {
                          "success": false,
                          "message": "사용자를 찾을 수 없습니다.",
                          "data": null,
                          "status": 404
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/orders")
    CommonResponse<MockOrderDto.OrderSummaryResponseDto> createOrder(
            @RequestBody MockOrderDto.OrderRequestDto request
    );

    @Operation(
            summary = "사용자 주문 내역 조회",
            description = "특정 사용자의 전체 주문 이력을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "사용자 없음 예시",
                                            value = """
                        {
                          "success": false,
                          "message": "사용자를 찾을 수 없습니다.",
                          "data": null,
                          "status": 404
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/users/{userId}/orders")
    CommonResponse<List<MockOrderDto.UserOrderSummaryDto>> getUserOrders(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId
    );

    @Operation(
            summary = "주문 상세 조회",
            description = "특정 주문 ID에 대한 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "주문 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "주문 없음 예시",
                                            value = """
                        {
                          "success": false,
                          "message": "주문을 찾을 수 없습니다.",
                          "data": null,
                          "status": 404
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/orders/{orderId}")
    CommonResponse<MockOrderDto.OrderDetailDto> getOrderDetail(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long orderId
    );

    @Operation(
            summary = "결제 내역 조회",
            description = "주문 ID에 대한 결제 내역을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "주문 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "결제 내역 없음 예시",
                                            value = """
                        {
                          "success": false,
                          "message": "결제 정보를 찾을 수 없습니다.",
                          "data": null,
                          "status": 404
                        }
                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/orders/{orderId}/payment")
    CommonResponse<MockOrderDto.OrderPaymentDto> getOrderPayment(
            @Parameter(description = "주문 ID", required = true)
            @PathVariable Long orderId
    );
}
