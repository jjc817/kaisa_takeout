package com.kaisa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.common.R;
import com.kaisa.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {

    R<ShoppingCart> sub(ShoppingCart shoppingCart);

    R<String> clean();
}
