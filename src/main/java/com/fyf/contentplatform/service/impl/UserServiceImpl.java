package com.fyf.contentplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyf.contentplatform.dto.LoginDTO;
import com.fyf.contentplatform.dto.RegisterDTO;
import com.fyf.contentplatform.entity.SysUser;
import com.fyf.contentplatform.exception.BusinessException;
import com.fyf.contentplatform.mapper.SysUserMapper;
import com.fyf.contentplatform.service.UserService;
import com.fyf.contentplatform.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 13:12
 */
@Service
public class UserServiceImpl implements UserService {

    //构造器注入Mapper、BCrypt密码加密工具、JWT工具
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public UserServiceImpl(SysUserMapper sysUserMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 注册功能
    @Override
    public void register(RegisterDTO dto) {
        // 拿前端传回的用户名和数据库的用户名比较
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        if (sysUserMapper.selectCount(wrapper) > 0) {
            //抛出自定义业务异常，会被全局异常处理器捕获
            throw new BusinessException("用户名已存在");
        }

        //组装用户实体
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        // 密码用BCrypt加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // 昵称：如果前端传了nickname就用传入值，没传就默认和用户名一致
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setEmail(dto.getEmail());
        //插入到用户表
        sysUserMapper.insert(user);
    }

    //登录功能
    @Override
    public Map<String, Object> login(LoginDTO dto) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", dto.getUsername());
        SysUser user = sysUserMapper.selectOne(wrapper);

        //用户名查不到或者密码不匹配，抛出自定义业务异常，会被全局异常处理器捕获
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        //生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        //打包返回token、id、用户名、昵称
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        return result;
    }
}
