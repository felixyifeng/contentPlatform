package com.fyf.contentplatform.controller;

import com.fyf.contentplatform.annotation.RequireRole;
import com.fyf.contentplatform.common.Result;
import com.fyf.contentplatform.entity.Category;
import com.fyf.contentplatform.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 17:28
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    //构造器注入CategoryService
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //查询所有分类，返回每一条分类记录
    @GetMapping
    public Result<List<Category>> list() {
        return Result.success(categoryService.listAll());
    }

    //新增分类
    @PostMapping
    public Result<?> save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success();
    }

    //修改分类
    @PutMapping
    public Result<?> update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }

    //根据id删除分类
    @RequireRole("admin")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}