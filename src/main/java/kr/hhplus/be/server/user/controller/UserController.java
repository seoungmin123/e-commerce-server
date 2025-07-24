package kr.hhplus.be.server.user.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.user.controller.swagger.UserSwaggerDocs;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.PointChargeRequestDto;
import kr.hhplus.be.server.user.dto.PointUseRequestDto;
import kr.hhplus.be.server.user.dto.UserResponseDto;
import kr.hhplus.be.server.user.facade.UserPointFacade;
import kr.hhplus.be.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserSwaggerDocs {

    private final UserService userService;
    private final UserPointFacade userPointFacade;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/")
    public ApiResponse<List<UserResponseDto>> getUser () {
        // 전체 사용자 조회
        List<UserResponseDto> users = userService.getUser();
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, users);
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponseDto> getUserInfo (@PathVariable Long userId) {
        //특정 사용자 조회
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, userInfo);
    }

    // 유저 포인트 조회 로직
    @GetMapping("/points/{userId}")
    public ApiResponse<PointBalanceDto> getPoint(@PathVariable Long userId) {
        PointBalanceDto pointInfo = userService.getPointBalance(userId);

        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, pointInfo);
    }

    // 포인트 충전 로직
    @PatchMapping("/points/{userId}/charge")
    public ApiResponse<PointBalanceDto> chargePoint(@PathVariable Long userId, @RequestBody PointChargeRequestDto request) {
        PointBalanceDto pointInfo = userPointFacade.chargePoint(userId,request);
        return ApiResponse.success(ApiResponseCode.SUCCESS_CREATED_201, pointInfo);
    }

    // 포인트 사용 로직
    @PatchMapping("/points/{userId}/use")
    public ApiResponse<PointBalanceDto> usePoint(@PathVariable Long userId, @RequestBody PointUseRequestDto request) {
        PointBalanceDto pointInfo = userPointFacade.usePoint(userId,request);

        return ApiResponse.success(ApiResponseCode.SUCCESS_CREATED_201, pointInfo);
    }

}