package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("CouponService 단위 테스트")
class CouponServiceTest {

    private CouponRepository couponRepository;
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        couponRepository = mock(CouponRepository.class);
        couponService = new CouponService(couponRepository);
    }

    @Nested
    @DisplayName("getUserCoupons 메서드 호출 시")
    class GetUserCoupons {

        @Test
        @DisplayName("유저가 보유한 쿠폰 목록을 반환한다")
        void 유저의_쿠폰목록을_조회한다() {
            // given
            Long userId = 1L;
            Coupon coupon1 = new Coupon(10L, userId, "10% 할인", 10, LocalDateTime.now().plusDays(3));
            Coupon coupon2 = new Coupon(11L, userId, "20% 할인", 20, LocalDateTime.now().plusDays(5));
            when(couponRepository.findAllByUserId(userId)).thenReturn(List.of(coupon1, coupon2));

            // when
            List<IssuedCouponResponseDto> result = couponService.getUserCoupons(userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("10% 할인");
            assertThat(result.get(1).discountRate()).isEqualTo(20);
            verify(couponRepository).findAllByUserId(userId);
        }
    }
}