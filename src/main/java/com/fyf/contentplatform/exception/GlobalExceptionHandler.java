package com.fyf.contentplatform.exception;

import com.fyf.contentplatform.common.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 12:24
 */
//全局拦截所有 Controller 抛出的异常，返回值直接序列化成 JSON
@RestControllerAdvice
public class GlobalExceptionHandler {

    //专门处理参数校验失败，返回 code=400 + 具体错误信息
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidation(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return Result.error(400, message);
    }

    //专门处理运行时抛出异常，返回 code=400 + 具体错误信息
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusiness(BusinessException e) {
        return Result.error(400, e.getMessage());
    }

    //兜底处理所有其他异常，返回 code=500
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();  // 开发阶段打印堆栈，方便排查
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }

}
