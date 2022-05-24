package com.kaisa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaisa.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
