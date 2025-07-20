package kr.hhplus.be.server.mock.controller;

import kr.hhplus.be.server.mock.controller.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockExternalOrderDto;
import kr.hhplus.be.server.mock.controller.dto.MockOrderDto.*;
import kr.hhplus.be.server.mock.controller.dto.OrderStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mock")
public class OrderController implements OrderControllerSpec {

    /** 주문요청*/
    @PostMapping("/orders")
    public CommonResponse<OrderSummaryResponseDto> createOrder(@RequestBody OrderRequestDto request) {

        String status = "SUCCESS"; // "FAIL";
        String reason = null; //"쿠폰만료";


        //TODO 비동기 처리
        MockExternalOrderDto.ExternalOrderRequestDto external = new MockExternalOrderDto.ExternalOrderRequestDto(
                100L,
                request.userId(),
                18000,
                status,
                reason,
                request.items()
        );


        return CommonResponse.success("주문 완료", new OrderSummaryResponseDto(
                100L,
                OrderStatus.COMPLETED,
                18000
        ), 200);
    }

    /** 주문내역 조회*/
    @GetMapping("/users/{userId}/orders")
    public CommonResponse<List<UserOrderSummaryDto>> getUserOrders(@PathVariable Long userId) {
        return CommonResponse.success("성공", List.of(
                new UserOrderSummaryDto(
                        100L,
                        LocalDateTime.of(2025, 7, 17, 16, 0),
                        18000,
                        OrderStatus.COMPLETED
                )
        ), 200);
    }

    /** 주문 상세내역 조회*/
    @GetMapping("/orders/{orderId}")
    public CommonResponse<OrderDetailDto> getOrderDetail(@PathVariable Long orderId) {
        return CommonResponse.success("성공", new OrderDetailDto(
                orderId,
                LocalDateTime.of(2025, 7, 17, 16, 0),
                OrderStatus.COMPLETED,
                18000,
                5L,
                List.of(
                        new OrderItemDto(1L, 2, 9000, 18000)
                )
        ), 200);
    }

    /** 주문결제 내역 조회*/
    @GetMapping("/orders/{orderId}/payment")
    public CommonResponse<OrderPaymentDto> getOrderPayment(@PathVariable Long orderId) {
        return CommonResponse.success("성공", new OrderPaymentDto(
                20L,
                5L,
                20000,
                2000,
                18000,
                LocalDateTime.of(2025, 7, 17, 16, 2)
        ), 200);
    }

}
