package com.kaisa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaisa.common.R;
import com.kaisa.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    R<AddressBook> saveAddr(AddressBook addressBook);

    R<AddressBook> setDefault(AddressBook addressBook);

    AddressBook getDefault();

    R<List<AddressBook>> listAddr(AddressBook addressBook);
}
