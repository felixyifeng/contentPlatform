package com.fyf.contentplatform.interceptor;

import com.fyf.contentplatform.annotation.RequireRole;
import com.fyf.contentplatform.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 16:48
 */
// JWT登录拦截器，在 Controller 执行之前拦截
@Component
public class JwtInterceptor implements HandlerInterceptor {

    // 通过构造器注入JwtUtil
    private final JwtUtil jwtUtil;
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 登录、注册接口不经过这个拦截器，要在 Web 配置里排除掉
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 起作用前提，前端约定：把 token 放在请求头 `Authorization` 里面，格式为`Bearer token...`
        // 从请求头中获取名为 Authorization 的值，赋值给变量 token
        String token = request.getHeader("Authorization");
        // 判断 token 是否为空，且是否以 `Bearer ` 开头
        if (token != null && token.startsWith("Bearer ")) {
            // 去掉前面的 "Bearer " 前缀，拿到纯token字符串
            token = token.substring(7);
            // 校验token是否合法有效
            if (jwtUtil.validateToken(token)) {
                // 如果token合法有效，获取用户ID并存入请求属性中，方便后续获取
                request.setAttribute("userId", jwtUtil.getUserId(token));

                // 检查方法上是否有 @RequireRole 注解
                //判断拦截器拿到的请求处理器对象是不是Controller接口方法
                if (handler instanceof HandlerMethod) {
                    //handler 转成 HandlerMethod 类型，handlerMethod这个对象可以拿到 Controller 方法上面的注解
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    // 获取方法上的 RequireRole 注解，没写就返回 null
                    RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
                    // 如果方法上有 RequireRole 注解，需要进行权限校验
                    if (requireRole != null) {
                        // 从 token 中获取用户角色
                        String role = jwtUtil.getRole(token);
                        // 如果用户的角色和接口要求的角色不一致
                        if (!requireRole.value().equals(role)) {
                            // 设置HTTP响应状态码为403，表示权限不足
                            response.setStatus(403);
                            // 设置响应内容类型为 JSON
                            response.setContentType("application/json;charset=utf-8");
                            // 向前端返回固定的 JSON 格式数据
                            response.getWriter().write("{\"code\":403,\"message\":\"权限不足\",\"data\":null}");
                            // 拦截请求，不会进入Controller
                            return false;
                        }
                    }
                }
                //放行请求进入Controller
                return true;
            }
        }
        // 走到这里：没带token / 前缀不对 / token校验失败 → 拒绝访问
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":401,\"message\":\"未登录或token无效\",\"data\":null}");
        //拦截请求，不会进入controller
        return false;
    }
}
