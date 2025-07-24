package kr.hhplus.be.server.user.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.user.dto.*;
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
class UserControllerE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/user";
    }

    @Nested
    @DisplayName("GET /user/ping")
    class Ping {

        @Test
        @DisplayName("ping 응답은 pong이어야 한다")
        void ping_success() {
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl() + "/ping", String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("pong");
        }
    }

    @Nested
    @DisplayName("GET /user/")
    class GetAllUsers {

        @Test
        @DisplayName("전체 사용자 조회에 성공한다")
        void getUser_success() {
            ResponseEntity<ApiResponse<List<UserResponseDto>>> response = restTemplate.exchange(
                    baseUrl() + "/",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<List<UserResponseDto>> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(200);
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /user/{userId}")
    class GetUserInfo {

        @Test
        @DisplayName("특정 사용자 정보 조회에 성공한다")
        void getUserInfo_success() {
            Long userId = 1L;

            ResponseEntity<ApiResponse<UserResponseDto>> response = restTemplate.exchange(
                    baseUrl() + "/" + userId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<UserResponseDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(200);
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /user/points/{userId}")
    class GetUserPoint {

        @Test
        @DisplayName("포인트 잔액 조회에 성공한다")
        void getPoint_success() {
            Long userId = 1L;

            ResponseEntity<ApiResponse<PointBalanceDto>> response = restTemplate.exchange(
                    baseUrl() + "/points/" + userId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<PointBalanceDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(200);
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("PATCH /user/points/{userId}/charge")
    class ChargePoint {

        @Test
        @DisplayName("포인트 충전에 성공한다")
        void chargePoint_success() {
            Long userId = 1L;

            PointChargeRequestDto requestDto = new PointChargeRequestDto(1000L);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PointChargeRequestDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<ApiResponse<PointBalanceDto>> response = restTemplate.exchange(
                    baseUrl() + "/points/" + userId + "/charge",
                    HttpMethod.PATCH,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<PointBalanceDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // 실제로는 201이 나올 수도 있음
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(201);
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("PATCH /user/points/{userId}/use")
    class UsePoint {

        @Test
        @DisplayName("포인트 사용에 성공한다")
        void usePoint_success() {
            Long userId = 1L;

            PointUseRequestDto requestDto = new PointUseRequestDto(500L);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PointUseRequestDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<ApiResponse<PointBalanceDto>> response = restTemplate.exchange(
                    baseUrl() + "/points/" + userId + "/use",
                    HttpMethod.PATCH,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<PointBalanceDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // 실제로는 201이 나올 수도 있음
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(201);
            assertThat(body.getData()).isNotNull();
        }
    }
}