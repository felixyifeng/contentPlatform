package com.fyf.contentplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author fyf
 * @Description
 * @create 2026-11-22 16:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;//用户ID
    private String username;//用户名
    private String password;//密码
    private String nickname;//昵称
    private String email;//邮箱
    private String avatar;//头像URL
    private Integer status;//状态
    private String role;//角色
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
