package com.kaisa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.common.R;
import com.kaisa.entity.User;

public interface UserService extends IService<User> {
    R<String> sendCode(String phone);
}
