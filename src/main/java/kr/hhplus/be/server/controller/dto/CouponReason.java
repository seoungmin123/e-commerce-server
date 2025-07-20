package kr.hhplus.be.server.controller.dto;


public enum CouponReason {
    EXPIRED, //만료
    USED, //사용함
    NOT_OWNED //잘못된요청
}