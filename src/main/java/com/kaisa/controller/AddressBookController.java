package com.kaisa.controller;

import com.kaisa.common.R;
import com.kaisa.entity.AddressBook;
import com.kaisa.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        return addressBookService.saveAddr(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        return addressBookService.setDefault(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        AddressBook addressBook = addressBookService.getDefault();
        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        return addressBookService.listAddr(addressBook);
    }

    @PutMapping()
    public R<String> updateAddr(@RequestBody AddressBook addressBook){
        boolean isUpdate = addressBookService.updateById(addressBook);
        if(isUpdate){
            return R.success("修改地址成功！");
        }else {
            return R.error("修改地址失败！");
        }
    }
    @DeleteMapping()
    public R<String> deleteAddr(@RequestParam("ids") Long ids){
        boolean isDelete = addressBookService.removeById(ids);
        if (isDelete) {
            return R.success("删除地址成功！");
        }else {
            return R.error("删除地址失败！");
        }

    }
}

