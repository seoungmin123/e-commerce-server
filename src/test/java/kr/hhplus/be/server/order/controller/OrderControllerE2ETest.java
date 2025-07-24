package kr.hhplus.be.server.order.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.order.dto.*;
import kr.hhplus.be.server.product.dto.PopularProductResponseDto;
import kr.hhplus.be.server.support.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/orders";
    }

    @Nested
    @DisplayName("POST /orders")
    class CreateOrder {

        @Nested
        @DisplayName("쿠폰이 없는 경우")
        class WithoutCoupon {

            @Test
            @DisplayName("주문 요청에 성공한다")
            void requestOrderWithoutCoupon_success() {
                OrderRequestDto requestDto = OrderFixture.주문요청_쿠폰없음();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<OrderRequestDto> request = new HttpEntity<>(requestDto, headers);

                ResponseEntity<ApiResponse<OrderCreatedResponseDto>> response = restTemplate.exchange(
                        baseUrl(),
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<>() {}
                );

                ApiResponse<OrderCreatedResponseDto> body = response.getBody();

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(body).isNotNull();
                assertThat(body.isSuccess()).isTrue();
                assertThat(body.getData()).isNotNull();
            }
        }

        @Nested
        @DisplayName("쿠폰이 있는 경우")
        class WithCoupon {

            @Test
            @DisplayName("쿠폰을 적용하여 주문 요청에 성공한다")
            void requestOrderWithCoupon_success() {
                OrderRequestDto requestDto = OrderFixture.주문요청_쿠폰있음();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<OrderRequestDto> request = new HttpEntity<>(requestDto, headers);

                ResponseEntity<ApiResponse<OrderCreatedResponseDto>> response = restTemplate.exchange(
                        baseUrl(),
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<>() {}
                );

                ApiResponse<OrderCreatedResponseDto> body = response.getBody();

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(body).isNotNull();
                assertThat(body.isSuccess()).isTrue();
                assertThat(body.getData()).isNotNull();
            }
        }
    }


    @Nested
    @DisplayName("GET /orders/{userId}")
    class GetUserOrders {

        @Test
        @DisplayName("사용자 주문 목록 조회에 성공한다")
        void getOrderList_success() {
            Long userId = 1L;

            ResponseEntity<ApiResponse<List<OrderSummaryResponseDto>>> response = restTemplate.exchange(
                    baseUrl() + "/" + userId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<List<OrderSummaryResponseDto>> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /orders/{orderId}/payment")
    class GetOrderPayment {

        @Test
        @DisplayName("결제 내역 조회에 성공한다")
        void getOrderPayment_success() {
            Long orderId = 1L;

            ResponseEntity<ApiResponse<OrderPaymentResponseDto>> response = restTemplate.exchange(
                    baseUrl() + "/" + orderId + "/payment",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<OrderPaymentResponseDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /orders/{orderId}/detail")
    class GetOrderDetail {

        @Test
        @DisplayName("주문 상세 조회에 성공한다")
        void getOrderDetail_success() {
            Long orderId = 1L;

            ResponseEntity<ApiResponse<OrderDetailResponseDto>> response = restTemplate.exchange(
                    baseUrl() + "/" + orderId + "/detail",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<OrderDetailResponseDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /orders/popular")
    class GetPopularProducts {

        @Test
        @DisplayName("인기 상품 조회에 성공한다")
        void getPopularProducts_success() {
            int days = 7;
            int limit = 3;

            ResponseEntity<ApiResponse<List<PopularProductResponseDto>>> response = restTemplate.exchange(
                    baseUrl() + "/popular?days=" + days + "&limit=" + limit,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<List<PopularProductResponseDto>> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getData()).isNotNull();
        }
    }
}