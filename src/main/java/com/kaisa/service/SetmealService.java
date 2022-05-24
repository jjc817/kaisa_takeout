package com.kaisa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.dto.DishDto;
import com.kaisa.dto.SetmealDto;
import com.kaisa.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);



    public void removeWithDish(List<Long> ids);

}
