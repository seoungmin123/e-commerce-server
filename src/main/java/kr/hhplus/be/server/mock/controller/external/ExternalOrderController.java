package kr.hhplus.be.server.mock.controller.external;

import kr.hhplus.be.server.app.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockExternalOrderDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mock/external")
public class ExternalOrderController implements ExternalOrderControllerSpec{

    /** 주문 후 외부 api */
    @PostMapping("/orders")
    public CommonResponse<Void> sendOrderToExternal (@RequestBody MockExternalOrderDto.ExternalOrderRequestDto request) {
        if ("FAIL".equalsIgnoreCase(request.status())) {
            return CommonResponse.fail("외부 시스템 오류: " + request.reason(), null, 500);
        }

        return CommonResponse.success("외부 시스템 처리 완료", null, 200);
    }
}
