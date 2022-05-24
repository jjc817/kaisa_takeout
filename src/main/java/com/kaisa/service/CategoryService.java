package com.kaisa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);

}
