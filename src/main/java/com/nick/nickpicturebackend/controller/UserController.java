package com.nick.nickpicturebackend.controller;

import com.nick.nickpicturebackend.common.BaseResponse;
import com.nick.nickpicturebackend.common.ResultUtils;
import com.nick.nickpicturebackend.exception.ErrorCode;
import com.nick.nickpicturebackend.exception.ThrowUtils;
import com.nick.nickpicturebackend.model.dto.user.UserLoginRequest;
import com.nick.nickpicturebackend.model.dto.user.UserRegisterRequest;
import com.nick.nickpicturebackend.model.entity.User;
import com.nick.nickpicturebackend.model.vo.LoginUserVo;
import com.nick.nickpicturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户注册
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午10:21
 * @Version: 1.0
 * @Description: TODO
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }


    /**
     * 用户注册
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVo> userLogin(@RequestBody UserLoginRequest userLoginRequest,
                                               HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVo result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVo> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVo(loginUser));
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }

}
