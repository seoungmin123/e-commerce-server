package kr.hhplus.be.server.couponPolicy.reader;

import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.exception.CouponPolicyNotFoundException;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaCouponPolicyReader implements CouponPolicyReader {

    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    public CouponPolicy findByIdOrThrow(Long couponPolicyId) {
        return couponPolicyRepository.findById(couponPolicyId)
                .orElseThrow(() -> new CouponPolicyNotFoundException(couponPolicyId));
    }

    @Override
    public List<CouponPolicy> findAll() {
        return couponPolicyRepository.findAll();
    }


}