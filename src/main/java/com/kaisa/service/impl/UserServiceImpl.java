package com.kaisa.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaisa.common.R;
import com.kaisa.entity.User;
import com.kaisa.mapper.UserMapper;
import com.kaisa.service.UserService;
import com.kaisa.utils.RegexUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.kaisa.utils.RedisConstants.LOGIN_CODE_KEY;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Override
    public R<String> sendCode(String phone) {
        // 校验手机号码
        if(RegexUtils.isPhoneInvalid(phone)){
            return R.error("手机号不正确");
        }
        //发送随机六位验证码
        String code = RandomUtil.randomNumbers(6);
        //存入redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone,code,60, TimeUnit.SECONDS);
        // TODO 发送短信
        return R.success("success");
    }

//    @Override
//    public R<String> login(@RequestParam LoginFormDTO loginForm) {
//        log.info("登录信息：{}",loginForm);
//        // 校验手机号
//        String phone = loginForm.getPhone();
//        if(RegexUtils.isPhoneInvalid(phone)){
//            return R.error("手机号不正确!傻瓜");
//        }
//        // 校验验证码
//        String code = loginForm.getCode();
//        String trueCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
//        if(trueCode == null || !trueCode.equals(code)){
//            return R.error("验证码错误!");
//        }
//        // 查询用户是否存在
//        User user = query().eq("phone", phone).one();
//        if(user == null){
//            // 新建用户
//            user = createUser(phone);
//        }
//        // 7.保存用户信息到 redis中
//
//        // 将User对象转为HashMap存储
//        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
//                CopyOptions.create()
//                        .setIgnoreNullValue(true)
//                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
//        // 存储
//        String token = UUID.randomUUID().toString(true);
//        String tokenKey = LOGIN_USER_KEY + token;
//        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
//        // 7.4.设置token有效期
//        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
//
//        // 8.返回token
//        return Result.ok(token);
//
//    }
//    private User createUser(String phone) {
//        User user = new User();
//        user.setPhone(phone);
//        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
//        save(user);
//        return user;
//    }
}
