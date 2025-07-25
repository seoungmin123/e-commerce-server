package kr.hhplus.be.server.couponPolicy.facade;

import kr.hhplus.be.server.coupon.reader.CouponReader;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.dto.CouponPolicyResponseDto;
import kr.hhplus.be.server.couponPolicy.reader.CouponPolicyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CouponPolicyFacadeTest {

    private CouponPolicyReader couponPolicyReader;
    private CouponReader couponReader;
    private CouponPolicyFacade couponPolicyFacade;

    @BeforeEach
    void setUp() {
        couponPolicyReader = mock(CouponPolicyReader.class);
        couponReader = mock(CouponReader.class);
        couponPolicyFacade = new CouponPolicyFacade(couponPolicyReader, couponReader);
    }

    @Test
    void 유효_정책만_조회된다() {
        // given
        Long userId = 1L;

        CouponPolicy available = mock(CouponPolicy.class);
        CouponPolicy expired = mock(CouponPolicy.class);
        CouponPolicy alreadyIssued = mock(CouponPolicy.class);
        CouponPolicy noQuantity = mock(CouponPolicy.class);

        when(available.isExpired()).thenReturn(false);
        when(available.isQuantityAvailable()).thenReturn(true);
        when(available.getCouponPolicyId()).thenReturn(100L);

        when(expired.isExpired()).thenReturn(true);
        when(noQuantity.isExpired()).thenReturn(false);
        when(noQuantity.isQuantityAvailable()).thenReturn(false);
        when(alreadyIssued.isExpired()).thenReturn(false);
        when(alreadyIssued.isQuantityAvailable()).thenReturn(true);
        when(alreadyIssued.getCouponPolicyId()).thenReturn(200L);

        when(couponPolicyReader.findAll())
                .thenReturn(List.of(available, expired, noQuantity, alreadyIssued));

        when(couponReader.existsByUserIdAndCouponPolicyId(userId, 100L)).thenReturn(false); // available
        when(couponReader.existsByUserIdAndCouponPolicyId(userId, 200L)).thenReturn(true);  // alreadyIssued

        // when
        List<CouponPolicyResponseDto> result = couponPolicyFacade.getAvailablePolicies(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).couponPolicyId()).isEqualTo(100L);
    }
}