package com.fyf.contentplatform.vo;

import lombok.Data;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 18:03
 */
@Data
public class ArticleVO {
    private Long id;
    private String title;
    private String summary;
    private Integer viewCount;
    private String categoryName;
    private String authorName;
}
