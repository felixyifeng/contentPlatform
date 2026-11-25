package com.fyf.contentplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyf.contentplatform.entity.Article;
import com.fyf.contentplatform.vo.ArticleVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-22 21:34
 */
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT a.id, a.title, a.summary, a.view_count, c.name AS category_name, u.nickname AS author_name " +
            "FROM article a " +
            "LEFT JOIN article_category c ON a.category_id = c.id " +
            "LEFT JOIN sys_user u ON a.author_id = u.id " +
            "ORDER BY a.id DESC")
    List<ArticleVO> listWithDetails();
}
