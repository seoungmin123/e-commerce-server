package kr.hhplus.be.server.order.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.order.dto.*;
import kr.hhplus.be.server.order.facade.OrderFacade;
import kr.hhplus.be.server.order.service.OrderService;
import kr.hhplus.be.server.product.dto.PopularProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    // 주문 요청
    @PostMapping
    public ApiResponse<OrderCreatedResponseDto> requestOrder(@RequestBody OrderRequestDto request) {
        OrderCreatedResponseDto response = orderFacade.createOrder(request);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, response);
    }

    // 특정 사용자 주문 목록 조회
    @GetMapping("/{userId}")
    public ApiResponse<List<OrderSummaryResponseDto>> getOrderList(@PathVariable Long userId) {
        List<OrderSummaryResponseDto> result = orderService.getOrderListByUserId(userId);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, result);
    }

    // 특정 주문의 결제 내역 조회
    @GetMapping("/{orderId}/payment")
    public ApiResponse<OrderPaymentResponseDto> getOrderPayment(@PathVariable Long orderId) {
        OrderPaymentResponseDto result = orderService.getOrderPayment(orderId);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, result);
    }

    // 주문 상세 조회 (아이템 + 결제 포함)
    @GetMapping("/{orderId}/detail")
    public ApiResponse<OrderDetailResponseDto> getOrderDetail(@PathVariable Long orderId) {
        OrderDetailResponseDto result = orderService.getOrderDetail(orderId);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, result);
    }

    // 인기 상품 조회
    @GetMapping("/popular")
    public ApiResponse<List<PopularProductResponseDto>> getPopularProducts(@RequestParam int days, @RequestParam int limit) {
        List<PopularProductResponseDto> result = orderFacade.getPopularProducts(days, limit);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, result);
    }
}