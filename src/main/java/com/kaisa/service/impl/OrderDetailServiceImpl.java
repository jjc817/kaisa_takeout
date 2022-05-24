package com.kaisa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.entity.OrderDetail;
import com.kaisa.mapper.OrderDetailMapper;
import com.kaisa.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
