package com.kaisa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaisa.common.R;
import com.kaisa.dto.DishDto;
import com.kaisa.entity.*;
import com.kaisa.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
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
        for (Long id : ids) {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId,id).set(Dish::getStatus,0);
            //查询该菜品出现在那些套餐中
            LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
            setmealDishWrapper.eq(SetmealDish::getDishId,id);
            List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishWrapper);
            //同时停用对应套餐
            for (SetmealDish setmealDish : setmealDishes) {
                Long setmealId = setmealDish.getSetmealId();
                LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                setmealLambdaUpdateWrapper.eq(Setmeal::getId,setmealId).set(Setmeal::getStatus,0);
                setmealService.update(setmealLambdaUpdateWrapper);
            }

            dishService.update(updateWrapper);

        }

        return R.success("停售菜品成功");
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
    public R<String> delete(Long[] ids){
        for (Long id : ids) {

            dishService.removeById(id);
        }
       return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
