package com.example.Restaurant.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Restaurant.system.common.Result;
import com.example.Restaurant.system.entity.User;
import com.example.Restaurant.system.entity.res.PasswordChanger;
import com.example.Restaurant.system.service.UserServiceImpl;
import com.example.Restaurant.system.untils.CheckUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserServiceImpl userService;

    @PostMapping("/login")
    public Result<?> login(@NotNull @RequestBody User user) {
        User res = userService.getById(user.getID());
        if (res == null) {
            return Result.error(-1, "工号错误");
        }
        if (!Objects.equals(res.getPassword(), user.getPassword())) {
            return Result.error(-1, "密码错误");
        }
        if (res.getStatus() == 1) {
            return Result.error(-1, "账号已被注销");
        }
        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@NotNull @RequestBody User user) {
        if (Objects.equals(user.getName(), ""))
            return Result.error(-1, "名字必须存在");
        if (Objects.equals(user.getPassword(), ""))
            return Result.error(-1, "密码必须存在");
        if (user.getPassword().length() < 8)
            return Result.error(-1, "密码不得小于8位");
        if (!(user.getJob() == 0 || user.getJob() == 1))
            return Result.error(-1, "职工类型未正确填写");
        user.setStatus(0);//注册默认激活
        userService.save(user);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<?> update(@NotNull @RequestBody User user) {
        //空电话更新
        if (user.getTelephone() == null) {
            userService.updateById(user);
            User res = userService.getById(user.getID());
            return Result.success(res);
        }
        //合法电话更新
        if (user.getTelephone() != null && CheckUtil.isMobile(user.getTelephone().toString())) {
            userService.updateById(user);
            User res = userService.getById(user.getID());
            return Result.success(res);
        }
        return Result.error(-1, "所填手机号不合法");
    }
    @PostMapping("/delete")
    public Result<?> delete(@NotNull @RequestBody User user) {
        if(user.getStatus()!=0)
            return Result.error(-1, "用户状态异常");
        if (user.getJob() == 1)
            return Result.error(-1, "经理不能直接被注销");
        user.setStatus(1);//注销
        userService.updateById(user);
        return Result.success();
    }

    @PostMapping("/updatePassword")
    public Result<?> updatePassword(@NotNull @RequestBody PasswordChanger passwordChanger) {
        User res = userService.getById(passwordChanger.getUserID());
        if (!Objects.equals(res.getPassword(), passwordChanger.getOriginalPassword()))
            return Result.error(-1, "原密码错误");
        //更新
        res.setPassword(passwordChanger.getNewPassword());
        userService.updateById(res);
        return Result.success(res);
    }

    @GetMapping("/view")
    public Result<?> viewPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<User> userWrapper = Wrappers.lambdaQuery();
        userWrapper.eq(User::getStatus,0);//仅选择已激活员工
        Page<User> UserPage = userService.page(new Page<>(pageNum, pageSize),userWrapper);
        return Result.success(UserPage);
    }

    @GetMapping("/find")
    public Result<?> find(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getStatus,0);//仅选择已激活员工
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search))
                wrapper.eq(User::getID, Integer.parseInt(search));
            else
                wrapper.like(User::getName, search);
        }
        Page<User> userPage = userService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }
}
