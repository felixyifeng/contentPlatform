package com.fyf.contentplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 13:09
 */
@Data
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
