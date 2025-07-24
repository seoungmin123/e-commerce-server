package kr.hhplus.be.server.infra.external.order;

public record ExternalOrderResponseDto(
        Boolean success,
        String externalOrderId,
        String message
) {}
