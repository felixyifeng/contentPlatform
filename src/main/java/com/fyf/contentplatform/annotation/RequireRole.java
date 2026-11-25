package com.fyf.contentplatform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fyf
 * @Description 自定义注解，用于方法级别权限控制
 * @create 2026-11-24 22:11
 */
@Target(ElementType.METHOD)// 规定该拦截器注解只能写在接口方法上面
@Retention(RetentionPolicy.RUNTIME)// 运行时保留，让拦截器可以读到这个注解
public @interface RequireRole {
    String value();  // 允许的角色，如 "admin"
}