package com.nick.nickpicturebackend.exception;

/**
 * 异常处理工具类
 *
 * @Author: Nick
 * @Date: 2025/3/9 下午7:46
 * @Version: 1.0
 * @Description: 异常处理工具类
 */

public class ThrowUtils {

    /**
     * 条件成立时抛出业务异常
     *
     * @param condition 条件
     * @param exception 异常
     */
    public static void throwIf(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }

    /**
     * 条件成立时抛出业务异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立时抛出业务异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

}
