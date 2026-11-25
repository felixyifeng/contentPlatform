package com.fyf.contentplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author fyf
 * @Description
 * @create 2026-11-24 16:30
 */
@Data
public class CommentDTO {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
