package com.kaisa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.common.BaseContext;
import com.kaisa.common.R;
import com.kaisa.entity.ShoppingCart;
import com.kaisa.mapper.ShoppingCartMapper;
import com.kaisa.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public R<ShoppingCart> sub(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId, shoppingCart.getDishId());
        shoppingCart = getOne(queryWrapper);
        if(shoppingCart.getNumber() == 1){
            cleanById(shoppingCart);
        }
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId())
                .set(ShoppingCart::getNumber,shoppingCart.getNumber()-1);
        boolean update = update(wrapper);
        if (update) {
            return R.success(shoppingCart);
        }else {
            return R.error("操作失败");
        }
    }

    @Override
    public R<String> clean() {

        //SQL:delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        remove(queryWrapper);

        return R.success("清空购物车成功");
    }

    private void cleanById(ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        remove(wrapper);
    }
}
