package com.kaisa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaisa.common.R;
import com.kaisa.dto.DishDto;
import com.kaisa.dto.SetmealDto;
import com.kaisa.entity.Setmeal;
import com.kaisa.entity.SetmealDish;
import com.kaisa.service.CategoryService;
import com.kaisa.service.DishService;
import com.kaisa.service.SetmealDishService;
import com.kaisa.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private DishService dishService;


    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        return setmealService.pageSetmeal(page,pageSize,name,categoryService);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);

        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> stopSell(Long[] ids) {
        for (Long id : ids) {
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Setmeal::getId, id).set(Setmeal::getStatus, 0);

            setmealService.update(updateWrapper);

        }

        return R.success("停售菜品成功");
    }

    /**
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> openSell(Long[] ids) {
        return setmealService.openSell(ids,dishService);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);
        return R.success(setmealDto);
    }

    @PutMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        return setmealService.updateSetmeal(setmealDto);
    }


    /**
     * 移动端点击套餐图片查看套餐具体内容
     * 这里返回的是dto 对象，因为前端需要copies这个属性
     * 前端主要要展示的信息是:套餐中菜品的基本信息，图片，菜品描述，以及菜品的份数
     * @param setmealId
     * @return
     */
    //这里前端是使用路径来传值的，要注意，不然你前端的请求都接收不到，就有点尴尬哈
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") Long setmealId){
        return setmealService.DetailOfSetmeal(setmealId,dishService);
    }
}

