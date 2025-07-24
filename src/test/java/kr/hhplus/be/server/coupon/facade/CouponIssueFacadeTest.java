package kr.hhplus.be.server.coupon.facade;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
import kr.hhplus.be.server.coupon.exception.AlreadyIssuedCouponException;
import kr.hhplus.be.server.coupon.reader.CouponReader;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.reader.CouponPolicyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("CouponIssueFacade 단위 테스트")
class CouponIssueFacadeTest {

    private CouponPolicyReader couponPolicyReader;
    private CouponReader couponReader;
    private CouponRepository couponRepository;
    private CouponIssueFacade couponIssueFacade;

    @BeforeEach
    void setUp() {
        couponPolicyReader = mock(CouponPolicyReader.class);
        couponReader = mock(CouponReader.class);
        couponRepository = mock(CouponRepository.class);
        couponIssueFacade = new CouponIssueFacade(couponPolicyReader, couponReader, couponRepository);
    }

    @Nested
    @DisplayName("쿠폰 발급 시")
    class IssueCoupon {

        @Test
        @DisplayName("정상적으로 발급된다")
        void 쿠폰_정상_발급() {
            // given
            Long userId = 1L;
            Long policyId = 10L;
            CouponPolicy policy = mock(CouponPolicy.class);
            Coupon coupon = mock(Coupon.class);

            when(couponPolicyReader.findByIdOrThrow(policyId)).thenReturn(policy);
            when(couponReader.existsByUserIdAndCouponPolicyId(userId, policyId)).thenReturn(false);
            when(policy.getCouponPolicyId()).thenReturn(policyId);
            when(policy.getName()).thenReturn("10% 할인");
            when(policy.getDiscountRate()).thenReturn(10);
            when(policy.getExpiredDt()).thenReturn(LocalDateTime.now().plusDays(3));
            when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

            // when
            IssuedCouponResponseDto response = couponIssueFacade.issue(userId, policyId);

            // then
            assertThat(response).isNotNull();
            verify(policy).validateIssueAvailability();
            verify(policy).decreaseIssuedRemain();
            verify(couponRepository).save(any(Coupon.class));
        }

        @Test
        @DisplayName("이미 발급받은 쿠폰이면 예외가 발생한다")
        void 중복_쿠폰_발급시_예외() {
            Long userId = 1L;
            Long policyId = 10L;
            CouponPolicy policy = mock(CouponPolicy.class);

            when(couponPolicyReader.findByIdOrThrow(policyId)).thenReturn(policy);
            when(couponReader.existsByUserIdAndCouponPolicyId(userId, policyId)).thenReturn(true);

            assertThatThrownBy(() -> couponIssueFacade.issue(userId, policyId))
                    .isInstanceOf(AlreadyIssuedCouponException.class)
                    .hasMessageContaining("userId: " + userId);
        }

        @Test
        @DisplayName("쿠폰 수량이 부족하면 예외가 발생한다")
        void 수량이_0이면_발급불가() {
            Long userId = 1L;
            Long policyId = 10L;
            CouponPolicy policy = mock(CouponPolicy.class);

            when(couponPolicyReader.findByIdOrThrow(policyId)).thenReturn(policy);
            when(couponReader.existsByUserIdAndCouponPolicyId(userId, policyId)).thenReturn(false);
            doThrow(new IllegalStateException("수량 없음")).when(policy).validateIssueAvailability();

            assertThatThrownBy(() -> couponIssueFacade.issue(userId, policyId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("수량 없음");
        }
    }
}