package kr.hhplus.be.server.infra.external.order;


import kr.hhplus.be.server.common.exception.ApiControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExternalOrderSender {

    private static final Logger log = LoggerFactory.getLogger(ApiControllerAdvice.class);

    public void send(ExternalOrderRequestDto request) {
        // 단순 로그 출력
        log.info("[외부 주문 전송 로그] orderId={}, userId={}, totalPrice={}, items={}",
                request.orderId(), request.userId(), request.totalPrice(), request.items());
    }
}