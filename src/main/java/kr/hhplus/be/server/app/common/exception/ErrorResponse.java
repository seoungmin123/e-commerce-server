package kr.hhplus.be.server.app.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
