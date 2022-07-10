package com.kaisa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.common.BaseContext;
import com.kaisa.common.R;
import com.kaisa.entity.AddressBook;
import com.kaisa.mapper.AddressBookMapper;
import com.kaisa.service.AddressBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    public R<AddressBook> saveAddr(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        save(addressBook);
        return R.success(addressBook);
    }

    @Override
    public R<AddressBook> setDefault(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        updateById(addressBook);
        return R.success(addressBook);
    }

    @Override
    public AddressBook getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = getOne(queryWrapper);
        return addressBook;
    }

    @Override
    public R<List<AddressBook>> listAddr(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(list(queryWrapper));
    }
}
