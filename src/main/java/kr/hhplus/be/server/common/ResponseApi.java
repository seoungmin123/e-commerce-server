package kr.hhplus.be.server.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통 API 응답 포맷
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseApi<T> {

    private boolean success;  // 요청 성공 여부
    private String message;   // 응답 메시지
    private T data;           // 응답 데이터
    private Integer status;   // HTTP 유사 상태 코드 (Mock용)

    // 성공 응답 생성자
    public ResponseApi(boolean success, String message, T data) {
        this(success, message, data, 200);
    }

    /**
     * 성공 응답 반환 (기본 메시지: "성공", 상태 코드: 200)
     */
    public static <T> ResponseApi<T> success(T data) {
        return new ResponseApi<>(true, "성공", data, 200);
    }

    /**
     * 성공 응답 반환 (커스텀 메시지)
     */
    public static <T> ResponseApi<T> success(String message, T data , Integer status ) {
        return new ResponseApi<>(true, message, data, status);
    }

    /**
     * 실패 응답 반환 (null 데이터)
     */
    public static <T> ResponseApi<T> fail(String message, int status) {
        return new ResponseApi<>(false, message, null, status);
    }

    /**
     * 실패 응답 반환 (데이터 포함)
     */
    public static <T> ResponseApi<T> fail(String message, T data, int status) {
        return new ResponseApi<>(false, message, data, status);
    }


}
