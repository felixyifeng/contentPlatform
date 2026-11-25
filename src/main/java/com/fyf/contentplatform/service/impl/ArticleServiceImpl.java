package com.fyf.contentplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fyf.contentplatform.entity.Article;
import com.fyf.contentplatform.mapper.ArticleMapper;
import com.fyf.contentplatform.service.ArticleService;
import com.fyf.contentplatform.vo.ArticleVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 10:38
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    //Redis缓存key的固定前缀
    private static final String CACHE_KEY_PREFIX = "article:";

    // 构造器自动注入acticleMapper、RedisTemplate
    private final ArticleMapper articleMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    public ArticleServiceImpl(ArticleMapper articleMapper, RedisTemplate<String, Object> redisTemplate) {
        this.articleMapper = articleMapper;
        this.redisTemplate = redisTemplate;
    }

    // 查询所有文章
    @Override
    public List<ArticleVO> listAll() {
        return articleMapper.listWithDetails();
    }

    // 根据id查询文章
    @Override
    public Article getById(Long id) {
        //拼接缓存key
        String key = CACHE_KEY_PREFIX + id;

        // 1. 先查缓存
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            System.out.println("【缓存命中】从Redis取文章 id=" + id);
            //从缓存中获取的数据强转为Article类型返回
            return (Article) cached;
        }

        // 2. 缓存没有，查数据库
        System.out.println("【缓存未命中】从数据库查文章 id=" + id);
        Article article = articleMapper.selectById(id);

        // 3. 查到后放入缓存，设置过期时间（5分钟）
        if (article != null) {
            redisTemplate.opsForValue().set(key, article, Duration.ofMinutes(5));
        }
        return article;
    }

    // 分页查询文章
    @Override
    public Page<Article> page(int pageNum, int pageSize) {
        return articleMapper.selectPage(new Page<>(pageNum, pageSize), null);
    }

    // 根据标题或分类查询文章
    @Override
    public List<Article> search(String title, Integer categoryId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.like(title != null && !title.isEmpty(), "title", title)
                .eq(categoryId != null, "category_id", categoryId);
        return articleMapper.selectList(wrapper);
    }

    //新增文章
    @Override
    @Transactional
    public void save(Article article) {
        articleMapper.insert(article);
    }

    //修改文章
    @Override
    @Transactional
    public void update(Article article) {
        articleMapper.updateById(article);
    }

    //根据id删除文章
    @Override
    @Transactional
    public void delete(Long id) {
        articleMapper.deleteById(id);
    }
}
