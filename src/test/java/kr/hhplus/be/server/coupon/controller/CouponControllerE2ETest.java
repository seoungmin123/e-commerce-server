package kr.hhplus.be.server.coupon.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
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
class CouponControllerE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/coupons";
    }

    @Nested
    @DisplayName("GET /coupons/{userId}")
    class GetUserCoupons {

        @Test
        @DisplayName("사용자의 보유 쿠폰 목록 조회에 성공한다")
        void getUserCoupons_success() {
            Long userId = 1L;

            ResponseEntity<ApiResponse<List<IssuedCouponResponseDto>>> response = restTemplate.exchange(
                    baseUrl() + "/" + userId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<List<IssuedCouponResponseDto>> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(200);
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("POST /coupons/{userId}")
    class IssueCoupon {

        @Test
        @DisplayName("쿠폰 발급에 성공한다")
        void issueCoupon_success() {
            Long userId = 1L;
            Long couponPolicyId = 1L; // ⚠️ 실제 존재하는 정책 ID 필요

            CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponPolicyId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CouponIssueRequestDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<ApiResponse<IssuedCouponResponseDto>> response = restTemplate.exchange(
                    baseUrl() + "/" + userId,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<IssuedCouponResponseDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // 또는 201
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(201);
            assertThat(body.getData()).isNotNull();
        }
    }
}