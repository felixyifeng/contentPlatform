package com.fyf.contentplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 12:22
 */
@Data
public class ArticleUpdateDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    //摘要
    private String summary;

    //封面URL
    private String cover;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "作者ID不能为空")
    private Long authorId;

    //浏览量
    private Integer viewCount;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
