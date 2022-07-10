package com.kaisa.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.common.R;
import com.kaisa.entity.Orders;

import java.util.Map;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

    R<Page> userPage(int page, int pageSize);

    R<String> again(Map<String, String> map);

    R<Page> pageOrder(int page, int pageSize, Long number, String beginTime, String endTime);
}
