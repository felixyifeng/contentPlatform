package com.fyf.contentplatform.service;

import com.fyf.contentplatform.dto.LoginDTO;
import com.fyf.contentplatform.dto.RegisterDTO;

import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 13:11
 */
public interface UserService {
    //注册用户
    void register(RegisterDTO dto);
    //用户登录,返回token、id、用户名、昵称
    Map<String, Object> login(LoginDTO dto);
}
