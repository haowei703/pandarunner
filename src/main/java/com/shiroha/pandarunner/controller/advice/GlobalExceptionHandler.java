package com.shiroha.pandarunner.controller.advice;

import cn.dev33.satoken.exception.NotLoginException;
import com.shiroha.pandarunner.common.R;
import com.shiroha.pandarunner.exception.AuthException;
import com.shiroha.pandarunner.exception.BusinessException;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.exception.NotSupportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        log.error("[{}]", e.getMessage(), e);
        return R.fail("error");
    }

    @ExceptionHandler(NotLoginException.class)
    public R<?> handleNotLoginException(NotLoginException e) {
        log.warn("[未登录的操作] {}", e.getMessage());
        return R.fail(401, "not login");
    }

    @ExceptionHandler(AuthException.class)
    public R<?> handleAuthException(AuthException e) {
        log.warn("[{}]", e.getMessage(), e);
        return R.fail(401, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        log.warn("[{}]", e.getMessage(), e);
        return R.fail(500, e.getMessage());
    }

    @ExceptionHandler(InvalidParamException.class)
    public R<?> handleInvalidParamException(InvalidParamException e) {
        log.warn("[{}]", e.getMessage());
        return R.fail(400, e.getMessage());
    }

    @ExceptionHandler(NotSupportException.class)
    public R<?> handleNotSupportException(NotSupportException e) {
        log.warn("[{}]", e.getMessage());
        return R.fail(400, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach((fieldError) -> {
            String message = fieldError.getDefaultMessage();
            log.warn("[{}] 字段:{} 得到的值:{}", message, fieldError.getField(), fieldError.getRejectedValue());
        });

        return R.fail(e.getStatusCode().value(), "参数格式不正确");
    }

    @ExceptionHandler(IOException.class)
    public R<?> handleIOException(IOException e) {
        log.warn("[{}]", e.getMessage());
        return R.fail(500, "请求失败");
    }
}
