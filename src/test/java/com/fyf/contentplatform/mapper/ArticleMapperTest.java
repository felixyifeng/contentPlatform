package com.fyf.contentplatform.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fyf.contentplatform.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fyf
 * @Description
 * @create 2026-11-22 21:36
 */
@SpringBootTest
@Transactional//每个测试方法在运行结束后自动回滚，不污染数据库
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void testSelectList() {
        // 查询所有文章
        List<Article> articles = articleMapper.selectList(null);
        articles.forEach(a -> System.out.println(a.getTitle() + " - " + a.getViewCount()));
    }

    @Test
    void testInsert() {
        // 新增文章
        Article article = new Article();
        article.setTitle("MP新增测试文章");
        article.setContent("这是内容");
        article.setSummary("测试摘要");
        article.setCategoryId(1L);
        article.setAuthorId(1L);
        article.setViewCount(0);
        article.setStatus(1);

        int rows = articleMapper.insert(article);
        System.out.println("影响行数：" + rows + "，生成ID：" + article.getId());
    }

    @Test
    void testQueryWrapper() {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)                    // status = 1
                .gt("view_count", 100)              // view_count > 100
                .orderByDesc("view_count");         // ORDER BY view_count DESC

        List<Article> articles = articleMapper.selectList(wrapper);
        articles.forEach(a -> System.out.println(a.getTitle() + " - " + a.getViewCount()));
    }

    @Test
    void testPage() {
        Page<Article> page = new Page<>(1, 2);  // 第1页，每页2条
        Page<Article> result = articleMapper.selectPage(page, null);  // null=无额外条件

        System.out.println("总记录数：" + result.getTotal());
        System.out.println("总页数：" + result.getPages());
        result.getRecords().forEach(a -> System.out.println(a.getTitle()));
    }



}
