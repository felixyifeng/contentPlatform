package com.fyf.contentplatform.service;

import com.fyf.contentplatform.dto.CommentDTO;
import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-24 16:32
 */
public interface CommentService {
    //根据文章ID查询评论列表
    List<Map<String, Object>> listByArticleId(Long articleId);
    //新增评论，传入评论DTO和用户ID
    void save(CommentDTO dto, Long userId);
    //删除评论，传入评论ID
    void delete(Long id);
}
