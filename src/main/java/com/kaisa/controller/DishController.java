package com.kaisa.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaisa.common.R;
import com.kaisa.dto.DishDto;
import com.kaisa.entity.Dish;
import com.kaisa.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;
    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private SetmealService setmealService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        return dishService.pageOfDish(page,pageSize,name,categoryService);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> stopSell(Long[] ids){
        return dishService.stopSell(ids,setmealService);

    }

    /**
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> openSell(Long[] ids){
        for (Long id : ids) {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId,id).set(Dish::getStatus,1);

            dishService.update(updateWrapper);

        }
        return R.success("起售菜品成功");
    }

    /**
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> delete(Long[] ids){
        for (Long id : ids) {

            dishService.removeById(id);
        }
       return R.success("删除成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "dishCache",key = "#dish.categoryId + '_' + #dish.status")
    public R<List<DishDto>> list(Dish dish){
        return dishService.listDish(dish,categoryService);
    }
}
