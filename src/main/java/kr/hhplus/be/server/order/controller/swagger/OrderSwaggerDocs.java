package kr.hhplus.be.server.order.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.order.dto.OrderDetailResponseDto;
import kr.hhplus.be.server.order.dto.OrderPaymentResponseDto;
import kr.hhplus.be.server.order.dto.OrderSummaryResponseDto;
import kr.hhplus.be.server.product.dto.PopularProductResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Order API", description = "주문 관련 API")
public interface OrderSwaggerDocs {

    @Operation(summary = "사용자 주문 목록 조회", description = "특정 사용자의 전체 주문 목록을 조회합니다.")
    @GetMapping("/{userId}")
    ApiResponse<List<OrderSummaryResponseDto>> getOrderList(
            @Parameter(description = "사용자 ID") @PathVariable Long userId);

    @Operation(summary = "주문 결제 내역 조회", description = "특정 주문의 결제 내역을 조회합니다.")
    @GetMapping("/{orderId}/payment")
    ApiResponse<OrderPaymentResponseDto> getOrderPayment(
            @Parameter(description = "주문 ID") @PathVariable Long orderId);

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}/detail")
    ApiResponse<OrderDetailResponseDto> getOrderDetail(
            @Parameter(description = "주문 ID") @PathVariable Long orderId);

    @Operation(summary = "인기 상품 조회", description = "최근 일정 기간 동안 가장 많이 팔린 상품을 조회합니다.")
    @GetMapping("/popular")
    ApiResponse<List<PopularProductResponseDto>> getPopularProducts(
            @Parameter(description = "조회 기간(일)") @RequestParam int days,
            @Parameter(description = "조회 개수") @RequestParam int limit);
}