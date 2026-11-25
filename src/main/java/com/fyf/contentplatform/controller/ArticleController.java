package com.fyf.contentplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fyf.contentplatform.annotation.RequireRole;
import com.fyf.contentplatform.common.Result;
import com.fyf.contentplatform.dto.ArticleSaveDTO;
import com.fyf.contentplatform.dto.ArticleUpdateDTO;
import com.fyf.contentplatform.entity.Article;
import com.fyf.contentplatform.service.ArticleService;
import com.fyf.contentplatform.vo.ArticleVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 10:46
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    //通过构造器注入ArticleService
    private final ArticleService articleService;
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    //获取所有文章
    @GetMapping
    public Result<List<ArticleVO>> list() {
        return Result.success(articleService.listAll());
    }

    //根据id获取文章
    @GetMapping("/{id}")
    public Result<Article> getById(@PathVariable Long id) {
        return Result.success(articleService.getById(id));
    }

    //分页获取文章
    @GetMapping("/page")
    public Result<Page<Article>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(articleService.page(pageNum, pageSize));
    }

    //搜索文章
    @GetMapping("/search")
    public Result<List<Article>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer categoryId) {
        return Result.success(articleService.search(title, categoryId));
    }

    //新增文章
    @PostMapping
    public Result<?> save(@Valid @RequestBody ArticleSaveDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setSummary(dto.getSummary());
        article.setCover(dto.getCover());
        article.setCategoryId(dto.getCategoryId());
        article.setAuthorId(dto.getAuthorId());
        article.setViewCount(dto.getViewCount());
        article.setStatus(dto.getStatus());
        articleService.save(article);
        return Result.success();
    }

    //修改文章
    @PutMapping
    public Result<?> update(@Valid @RequestBody ArticleUpdateDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setSummary(dto.getSummary());
        article.setCover(dto.getCover());
        article.setCategoryId(dto.getCategoryId());
        article.setAuthorId(dto.getAuthorId());
        article.setViewCount(dto.getViewCount());
        article.setStatus(dto.getStatus());
        articleService.update(article);
        return Result.success();
    }

    //根据id删除文章
    @RequireRole("admin")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        articleService.delete(id);
        return Result.success();
    }
}