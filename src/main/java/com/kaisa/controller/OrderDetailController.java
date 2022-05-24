package com.kaisa.controller;

import com.kaisa.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Resource
    private OrderDetailService orderDetailService;

}