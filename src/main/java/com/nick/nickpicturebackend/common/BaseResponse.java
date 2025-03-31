package com.nick.nickpicturebackend.common;

import com.nick.nickpicturebackend.exception.ErrorCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * 全局相应封装类
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午9:42
 * @Version: 1.0
 * @Description: 全局相应封装类
 */

@Getter
public class BaseResponse<T> implements Serializable {

    private final int code;

    private final T data;

    private final String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

}
