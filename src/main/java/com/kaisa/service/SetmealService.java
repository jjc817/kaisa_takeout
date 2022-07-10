package com.kaisa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.common.R;
import com.kaisa.dto.DishDto;
import com.kaisa.dto.SetmealDto;
import com.kaisa.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);



    public void removeWithDish(List<Long> ids);

    R<Page> pageSetmeal(int page, int pageSize, String name,CategoryService categoryService);

    R<String> openSell(Long[] ids,DishService dishService);

    R<List<DishDto>> DetailOfSetmeal(Long setmealId,DishService dishService);

    R<String> updateSetmeal(SetmealDto setmealDto);
}
