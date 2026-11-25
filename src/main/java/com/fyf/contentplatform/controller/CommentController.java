package com.fyf.contentplatform.controller;

import com.fyf.contentplatform.common.Result;
import com.fyf.contentplatform.dto.CommentDTO;
import com.fyf.contentplatform.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-24 16:43
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    //构造器注入Service
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //根据文章ID查询评论列表
    @GetMapping("/article/{articleId}")
    public Result<List<Map<String, Object>>> listByArticle(@PathVariable Long articleId) {
        return Result.success(commentService.listByArticleId(articleId));
    }

    //新增评论，传入评论DTO和用户ID
    @PostMapping
    public Result<?> save(@Valid @RequestBody CommentDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");  // 从拦截器取用户ID
        commentService.save(dto, userId);
        return Result.success();
    }

    //删除评论，传入评论ID
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }
}
