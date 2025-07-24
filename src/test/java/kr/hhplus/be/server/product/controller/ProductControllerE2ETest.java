package kr.hhplus.be.server.product.controller;


import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.order.dto.ProductListResponseDto;
import kr.hhplus.be.server.product.dto.ProductDetailResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class ProductControllerE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/product";
    }

    @Nested
    @DisplayName("GET /product/")
    class GetAllProducts {

        @Test
        @DisplayName("전체 상품 목록 조회에 성공한다")
        void getAllProducts_success() {
            ResponseEntity<ApiResponse<List<ProductListResponseDto>>> response = restTemplate.exchange(
                    baseUrl() + "/",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<List<ProductListResponseDto>> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(200);
            assertThat(body.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("GET /product/{productId}")
    class GetProduct {

        @Test
        @DisplayName("상품 상세 조회에 성공한다")
        void getProduct_success() {
            Long productId = 1L;  // 테스트용 상품 ID가 존재해야 성공함

            ResponseEntity<ApiResponse<ProductDetailResponseDto>> response = restTemplate.exchange(
                    baseUrl() + "/" + productId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse<ProductDetailResponseDto> body = response.getBody();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(body).isNotNull();
            assertThat(body.isSuccess()).isTrue();
            assertThat(body.getStatus()).isEqualTo(200);
            assertThat(body.getData()).isNotNull();
        }
    }
}