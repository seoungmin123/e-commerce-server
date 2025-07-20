package kr.hhplus.be.server.mock.controller.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
