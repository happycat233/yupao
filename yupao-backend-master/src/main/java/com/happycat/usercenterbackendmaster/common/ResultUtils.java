package com.happycat.usercenterbackendmaster.common;

/**
 * @author 吴昊
 * @version 1.0
 */
public class ResultUtils {
    /**
     * 成功返回
     *
     * @param data 数据
     * @param <T>  泛型
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(0, data, "ok");
    }

    public static ResponseResult<?> error(ErrorCode errorCode) {
        return new ResponseResult<>(errorCode);
    }

    public static ResponseResult<?> error(ErrorCode errorCode, String description) {
        return new ResponseResult<>(errorCode, description);
    }

    public static ResponseResult<?> error(int code, String message, String description) {
        return new ResponseResult<>(code, message, description);
    }
}
