package com.kaisa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.entity.User;
import com.kaisa.mapper.UserMapper;
import com.kaisa.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
