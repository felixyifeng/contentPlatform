package com.fyf.contentplatform.exception;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 13:14
 */
// 捕获运行时抛出的异常，交给@ExceptionHandler(BusinessException.class)处理
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
