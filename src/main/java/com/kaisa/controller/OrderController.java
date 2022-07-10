package com.kaisa.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaisa.common.R;
import com.kaisa.entity.Orders;
import com.kaisa.service.OrderDetailService;
import com.kaisa.service.OrderService;
import com.kaisa.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private OrderDetailService orderDetailService;

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
        return orderService.pageOrder(page,pageSize,number,beginTime,endTime);
    }


    /**
     * 用户端展示自己的订单分页查询
     * @param page
     * @param pageSize
     * @return
     * 遇到的坑：原来分页对象中的records集合存储的对象是分页泛型中的对象，里面有分页泛型对象的数据
     * 开始的时候我以为前端只传过来了分页数据，其他所有的数据都要从本地线程存储的用户id开始查询，
     * 结果就出现了一个用户id查询到 n个订单对象，然后又使用 n个订单对象又去查询 m 个订单明细对象，
     * 结果就出现了评论区老哥出现的bug(嵌套显示数据....)
     * 正确方法:直接从分页对象中获取订单id就行，问题大大简化了......
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        return orderService.userPage(page,pageSize);
    }



    @PutMapping
    public R<String> change(@RequestBody Orders order){
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getId,order.getId()).set(Orders::getStatus,order.getStatus());
        orderService.update(wrapper);
        return R.success("修改状态成功");
    }


    //客户端点击再来一单
    /**
     * 前端点击再来一单是直接跳转到购物车的，所以为了避免数据有问题，再跳转之前我们需要把购物车的数据给清除
     * ①通过orderId获取订单明细
     * ②把订单明细的数据的数据塞到购物车表中，不过在此之前要先把购物车表中的数据给清除(清除的是当前登录用户的购物车表中的数据)，
     * 不然就会导致再来一单的数据有问题；
     * (这样可能会影响用户体验，但是对于外卖来说，用户体验的影响不是很大，电商项目就不能这么干了)
     */
    @PostMapping("/again")
    public R<String> againSubmit(@RequestBody Map<String,String> map){
        return orderService.again(map);
    }
}
