package com.kaisa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.entity.DishFlavor;
import com.kaisa.mapper.DishFlavorMapper;
import com.kaisa.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
