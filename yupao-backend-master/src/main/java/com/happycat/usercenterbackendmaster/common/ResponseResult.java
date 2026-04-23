package com.happycat.usercenterbackendmaster.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 吴昊
 * @version 1.0
 */
@Data
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int code;
    private T data;
    private String message;
    private String description;

    public ResponseResult(int code, String message, T data, String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public ResponseResult(int code, T data, String message) {
        this(code, message, data, "");
    }

    public ResponseResult(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null, errorCode.getDescription());
    }

    public ResponseResult(ErrorCode errorCode, String description) {
        this(errorCode.getCode(), errorCode.getMessage(), null, description);
    }

    public ResponseResult(int code, String message, String description) {
        this(code, message, null, description);
    }

    public ResponseResult() {
    }
}
