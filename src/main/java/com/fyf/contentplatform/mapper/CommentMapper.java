package com.fyf.contentplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyf.contentplatform.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-24 16:25
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.id, c.article_id, c.content, c.create_time, u.nickname AS user_name " +
            "FROM article_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.id " +
            "WHERE c.article_id = #{articleId} " +
            "ORDER BY c.create_time DESC")
    List<Map<String, Object>> listByArticleId(Long articleId);
}
