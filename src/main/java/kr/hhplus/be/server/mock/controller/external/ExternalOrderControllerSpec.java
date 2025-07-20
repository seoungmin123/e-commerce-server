package kr.hhplus.be.server.mock.controller.external;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.app.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockExternalOrderDto;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "외부 주문 연동", description = "외부 시스템과 주문 연동 처리")
public interface ExternalOrderControllerSpec {

    @Operation(
            summary = "주문 완료 후 외부 시스템 전송",
            description = "주문 완료 후 외부 시스템(Mock API)으로 주문 정보를 전달합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MockExternalOrderDto.ExternalOrderRequestDto.class),
                            examples = @ExampleObject(
                                    name = "성공 예시",
                                    value = """
{
  "orderId": 100,
  "userId": 1,
  "totalAmount": 18000,
  "status": "SUCCESS",
  "reason": null,
  "items": [
    { "productId": 1, "item_cnt": 2 },
    { "productId": 2, "item_cnt": 1 }
  ]
}
"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "외부 시스템 처리 완료",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "성공 응답 예시",
                                            value = """
{
  "success": true,
  "message": "외부 시스템 처리 완료",
  "data": null,
  "status": 200
}
"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "외부 시스템 오류",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "실패 응답 예시",
                                            value = """
{
  "success": false,
  "message": "외부 시스템 오류: 재고 부족",
  "data": null,
  "status": 500
}
"""
                                    )
                            )
                    )
            }
    )
    public CommonResponse<Void> sendOrderToExternal(
            @RequestBody MockExternalOrderDto.ExternalOrderRequestDto request
    );
}