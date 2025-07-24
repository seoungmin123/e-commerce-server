package kr.hhplus.be.server.couponPolicy.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.couponPolicy.dto.CouponPolicyResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponPolicyControllerE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/coupon-policy";
    }

    @Test
    @DisplayName("사용자가 발급 가능한 쿠폰 정책 목록 조회에 성공한다")
    void getAvailablePolicies_success() {
        // given
        Long userId = 1L;  // DB에 존재하는 userId여야 함

        // when
        String url = baseUrl() + "/available?userId=" + userId;
        ResponseEntity<ApiResponse<List<CouponPolicyResponseDto>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        ApiResponse<List<CouponPolicyResponseDto>> body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getStatus()).isEqualTo(200);
        assertThat(body.getData()).isNotNull();
    }
}