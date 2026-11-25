package com.fyf.contentplatform.controller;

import com.fyf.contentplatform.common.Result;
import com.fyf.contentplatform.dto.LoginDTO;
import com.fyf.contentplatform.dto.RegisterDTO;
import com.fyf.contentplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 16:41
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 通过构造器注入UserService
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //注册接口
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    //登录接口，返回token、userId、username、nickname
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(userService.login(dto));
    }
}
