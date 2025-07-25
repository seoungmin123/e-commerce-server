package kr.hhplus.be.server.couponPolicy.reader;

import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;

import java.util.List;

public interface CouponPolicyReader {
    CouponPolicy findByIdOrThrow(Long couponPolicyId);
    List<CouponPolicy> findAll();

}