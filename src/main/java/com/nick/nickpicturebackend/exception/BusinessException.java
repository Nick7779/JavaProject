package com.nick.nickpicturebackend.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午6:48
 * @Version: 1.0
 * @Description: 自定义业务异常
 */

@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;


    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
