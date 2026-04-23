package com.happycat.usercenterbackendmaster.exception;

import com.happycat.usercenterbackendmaster.common.ErrorCode;
import com.happycat.usercenterbackendmaster.common.ResponseResult;
import com.happycat.usercenterbackendmaster.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 吴昊
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult handleBusinessException(final BusinessException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 处理系统异常
     *
     * @param e 业务异常
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult handleRunTimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统内部错误");
    }
}
