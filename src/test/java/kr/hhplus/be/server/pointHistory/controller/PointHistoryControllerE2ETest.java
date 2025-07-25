package kr.hhplus.be.server.pointHistory.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.pointHistory.dto.PointHistoryResponseDto;
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
class PointHistoryControllerE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/points";
    }

    @Test
    @DisplayName("포인트 이력 조회에 성공한다")
    void getPointHistory_success() {
        // given
        Long userId = 1L; // ⚠️ 테스트 유저와 이력 데이터가 있어야 함

        // when
        ResponseEntity<ApiResponse<List<PointHistoryResponseDto>>> response = restTemplate.exchange(
                baseUrl() + "/" + userId + "/history",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // then
        ApiResponse<List<PointHistoryResponseDto>> body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getStatus()).isEqualTo(200);
        assertThat(body.getData()).isNotNull();
    }
}