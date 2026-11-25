package com.fyf.contentplatform.service.impl;

import com.fyf.contentplatform.dto.CommentDTO;
import com.fyf.contentplatform.entity.Comment;
import com.fyf.contentplatform.mapper.CommentMapper;
import com.fyf.contentplatform.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-24 16:34
 */
@Service
public class CommentServiceImpl implements CommentService {

    //构造器自动注入Mapper
    private final CommentMapper commentMapper;
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    //根据文章ID查询评论列表
    @Override
    public List<Map<String, Object>> listByArticleId(Long articleId) {
        return commentMapper.listByArticleId(articleId);
    }

    //新增评论
    @Override
    @Transactional
    public void save(CommentDTO dto, Long userId) {
        Comment comment = new Comment();
        comment.setArticleId(dto.getArticleId());
        comment.setUserId(userId);  // 用户ID从token里取，不信任前端传的
        comment.setContent(dto.getContent());
        commentMapper.insert(comment);
    }

    //删除评论
    @Override
    @Transactional
    public void delete(Long id) {
        commentMapper.deleteById(id);
    }
}