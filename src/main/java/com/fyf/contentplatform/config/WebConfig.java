package com.fyf.contentplatform.config;

import com.fyf.contentplatform.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 17:03
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 通过构造器注入JwtInterceptor这个JWT登录拦截器
    private final JwtInterceptor jwtInterceptor;
    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //把这个 JWT 拦截器添加到拦截器链中
        registry.addInterceptor(jwtInterceptor)
                //要拦截的路径规则
                .addPathPatterns("/api/**")
                //排除、放行的路径：注册登录不需要token
                .excludePathPatterns("/api/users/**");
    }
}