package com.kaisa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaisa.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
