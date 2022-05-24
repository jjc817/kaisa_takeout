package com.kaisa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.entity.Employee;
import com.kaisa.mapper.EmployeeMapper;
import com.kaisa.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
