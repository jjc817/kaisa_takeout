package com.kaisa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaisa.common.BaseContext;
import com.kaisa.common.R;
import com.kaisa.entity.ShoppingCart;
import com.kaisa.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据:{}",shoppingCart);

        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if(dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else{
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if(cartServiceOne != null){
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        return shoppingCartService.clean();
    }
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
//        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
//        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
//        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId, shoppingCart.getDishId());
//        shoppingCart = shoppingCartService.getOne(queryWrapper);
//
//        log.info("购物车数据:{}",shoppingCart);
//        if(shoppingCart.getNumber()==1){
//            cleanOne(shoppingCart);
//        }
//        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
//        updateWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId, shoppingCart.getDishId());
//        updateWrapper.set(ShoppingCart::getNumber,shoppingCart.getNumber() - 1);
//
//        shoppingCartService.update(updateWrapper);
//
//        return R.success("成功");
        //Todo 第一次减页面的数据不会更改，购物车中数据正常
         return shoppingCartService.sub(shoppingCart);
    }
//    private void cleanOne(ShoppingCart shoppingCart){
//        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId())
//                .eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
//        shoppingCartService.remove(wrapper);
//    }



}