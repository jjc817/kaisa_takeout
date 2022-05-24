package com.kaisa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaisa.common.BaseContext;
import com.kaisa.common.R;
import com.kaisa.dto.DishDto;
import com.kaisa.entity.*;
import com.kaisa.service.OrderService;
import com.kaisa.service.ShoppingCartService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;
import sun.util.calendar.BaseCalendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    /**
     * 信息分页查询
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime ){
        log.info("page = {},pageSize = {},name = {},begin={},end={}" ,page,pageSize,number,beginTime,endTime);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.eq(number != null,Orders::getId,number);
        if(beginTime!=null){
            log.info("page = {},pageSize = {},name = {},begin={},end={}" ,page,pageSize,number,beginTime,endTime);
            final LocalDateTime begin = LocalDateTime.parse(beginTime, df);
            queryWrapper.ge(Orders::getOrderTime,begin);
        }
        if(endTime!=null){
            final LocalDateTime end = LocalDateTime.parse(endTime, df);
            queryWrapper.le(Orders::getOrderTime,end);

        }
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //执行查询
        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize,HttpSession session ){
        log.info("page = {},pageSize = {},name = {},begin={},end={}" ,page,pageSize);
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.eq(Orders::getUserId,session.getAttribute("user"));
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //执行查询
        orderService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> change(@RequestBody Orders order){
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getId,order.getId()).set(Orders::getStatus,order.getStatus());
        orderService.update(wrapper);
        return R.success("成功");
    }
    @PostMapping("/again")
    public R<String> again(Long id){
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Orders::getId, id);
//        Orders order = orderService.getOne(queryWrapper);
//        log.info("order is {}",order);
//        order.setOrderTime(LocalDateTime.now());
//        orderService.save(order);
        return R.success("再来一单成功");
    }
}
