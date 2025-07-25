package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.reader.CouponReader;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.reader.CouponPolicyReader;
import kr.hhplus.be.server.infra.external.order.ExternalOrderRequestDto;
import kr.hhplus.be.server.infra.external.order.ExternalOrderSender;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderItem;
import kr.hhplus.be.server.order.dto.OrderCreatedResponseDto;
import kr.hhplus.be.server.order.dto.OrderRequestDto;
import kr.hhplus.be.server.order.dto.ProductSalesDto;
import kr.hhplus.be.server.order.repository.OrderItemRepository;
import kr.hhplus.be.server.order.service.OrderService;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.dto.PopularProductResponseDto;
import kr.hhplus.be.server.product.reader.ProductReader;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductReader productReader;
    private final CouponReader couponReader;
    private final CouponPolicyReader couponPolicyReader;
    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    private final UserReader userReader;
    private final ExternalOrderSender externalOrderSender;

    private static final Logger log = LoggerFactory.getLogger(OrderFacade.class);

    /**
     * 유저 조회
     * 상품 + 재고 차감
     * 총액 계산
     * 쿠폰 할인 계산
     * 포인트 차감
     * 쿠폰사용 (만료 , 사용여부 검증)
     * 주문 + 결제 도메인 생성
     * 저장 및 응답
     * 외부 주문 전송
     * 주문상태바꾸기!
     * */
    public OrderCreatedResponseDto createOrder(OrderRequestDto requestDto) {
        //유저 조회
        User user = userReader.getUser(requestDto.userId());

        // 주문 아이템 리스트 생성 (상품명 조회 포함)
        List<OrderItem> orderItems = requestDto.items().stream()
                .map(dto -> {
                    Product product = productReader.getById(dto.productId());

                    //재고차감
                    product.deductStock(dto.quantity());

                    return OrderItem.create(product, dto.quantity());
                })
                .toList();

        // 총 주문 금액 계산
        Long originalPrice = orderItems.stream()
                .mapToLong(OrderItem::getTotalPrice)
                .sum();

        //쿠폰 조회 (null 가능 )
        Coupon coupon = null;
        long discountAmount = 0L;

        CouponPolicy policy = null;
        if (requestDto.couponId() != null) {
            coupon = couponReader.findByIdAndUserId(requestDto.couponId(), requestDto.userId());

            policy = couponPolicyReader.findByIdOrThrow(coupon.getCouponPolicyId());
            discountAmount = policy.calculateDiscount(originalPrice);

            try {
                coupon.markUsed();  //  사용 검증 , 만료검증 -> 사용처리
            } catch (IllegalStateException e) {
                log.warn("[쿠폰 사용 실패] userId={}, couponId={}, reason={}",
                        requestDto.userId(), coupon.getCouponId(), e.getMessage());

                throw e; // 그대로 던져서 공통 예외 핸들러로
            }
        }

        long finalPrice = originalPrice - discountAmount;

        try {
            // 포인트 차감
            user.usePoint(finalPrice);
        } catch (IllegalArgumentException e) {
            // TODO 실패 로그 (현재는 결재실패 상태 쓰지않음!!!  )
            log.warn("[포인트 차감 실패] userId={}, amount={}, reason={}",
                    user.getUserId(), finalPrice, e.getMessage());

            throw e; // 원래 흐름 유지
        }

        // 주문 생성 처리 -> 서비스
        Order order = orderService.createOrder(user, orderItems, originalPrice, policy);

        //TODO 주문 변경 (외부 연동 추후 변경)
        externalOrderSender.send(ExternalOrderRequestDto.from(order));

        return OrderCreatedResponseDto.from(order);

    }

    @Transactional(readOnly = true)
    public List<PopularProductResponseDto> getPopularProducts(int days, int limit) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days); // 오늘 - days (기준날짜)

        // 인기상품 그룹바이 집계 결과 조회 (productId, totalSold)
        List<ProductSalesDto> result = orderItemRepository.findTopSellingProducts(
                cutoff,
                PageRequest.of(0, limit)
        );

        // 각 productId에 대해 상품명 조회 및 응답 생성
        return result.stream()
                .map(p -> PopularProductResponseDto.from(p, productReader))
                .toList();
    }
}