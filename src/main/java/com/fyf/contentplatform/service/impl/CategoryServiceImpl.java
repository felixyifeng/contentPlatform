package com.fyf.contentplatform.service.impl;

import com.fyf.contentplatform.entity.Category;
import com.fyf.contentplatform.mapper.CategoryMapper;
import com.fyf.contentplatform.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 17:24
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    //构造器注入Mapper
    private final CategoryMapper categoryMapper;
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    //查询所有分类
    @Override
    public List<Category> listAll() {
        return categoryMapper.selectList(null);
    }

    //新增分类
    @Override
    @Transactional
    public void save(Category category) {
        categoryMapper.insert(category);
    }

    //修改分类
    @Override
    @Transactional
    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    //删除分类
    @Override
    @Transactional
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
