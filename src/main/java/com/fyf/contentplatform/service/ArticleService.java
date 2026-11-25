package com.fyf.contentplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fyf.contentplatform.entity.Article;
import com.fyf.contentplatform.vo.ArticleVO;

import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 10:34
 */
public interface ArticleService {

    //获取所有文章
    List<ArticleVO> listAll();

    //根据id获取文章
    Article getById(Long id);

    //分页获取文章
    Page<Article> page(int pageNum, int pageSize);

    //根据标题或分类获取文章
    List<Article> search(String title, Integer categoryId);

    //新增文章
    void save(Article article);

    //修改文章
    void update(Article article);

    //根据id删除文章
    void delete(Long id);
}
