package com.kaisa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.entity.AddressBook;
import com.kaisa.mapper.AddressBookMapper;
import com.kaisa.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
