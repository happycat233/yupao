package com.happycat.usercenterbackendmaster.exception;

import com.happycat.usercenterbackendmaster.common.ErrorCode;

/**
 * @author 吴昊
 * @version 1.0
 */

public class BusinessException extends RuntimeException {

    private final int code;
    private final String description;

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
