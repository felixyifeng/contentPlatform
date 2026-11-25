package com.fyf.contentplatform.common;

import lombok.Data;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 10:26
 */
@Data
public class Result<T> {
    private Integer code;// 状态码:200成功，500失败
    private String message;//提示信息
    private T data;//实际数据

    //成功，并且返回数据
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    //成功，不返回数据
    public static <T> Result<T> success() {
        return success(null);
    }

    //失败，使用默认错误码500，返回错误信息
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }

    //失败，使用自定义错误码和错误信息
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}
