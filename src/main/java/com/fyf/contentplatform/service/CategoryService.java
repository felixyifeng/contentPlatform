package com.fyf.contentplatform.service;

import com.fyf.contentplatform.entity.Category;
import java.util.List;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 17:21
 */
public interface CategoryService {
    // 查询所有分类
    List<Category> listAll();
    //新增分类
    void save(Category category);
    //修改分类
    void update(Category category);
    //删除分类
    void delete(Long id);
}
