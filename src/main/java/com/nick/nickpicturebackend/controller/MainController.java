package com.nick.nickpicturebackend.controller;

import com.nick.nickpicturebackend.common.BaseResponse;
import com.nick.nickpicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午10:21
 * @Version: 1.0
 * @Description: TODO
 */
@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查
     *
     * @return ok
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
