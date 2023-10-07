// UserController.java
package com.example.admin.controller;

import com.example.admin.entity.Response.ResponseObject;
import com.example.admin.entity.User.*;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 演示时使用的临时登录,不用查询邀请码,而是返回最近注册的用户的信息
    @RequestMapping(value="/templogin", method= RequestMethod.GET)
    public Login login2(String code) {
        return userService.templogin(code);
    }

    // 应用端输入验证码完成登录
    @RequestMapping(value="/login", method= RequestMethod.GET)
    public Login login(String code) {
        return userService.verifylogin(code);
    }

    // public ResponseObject login(String code) {
    //    return ResponseObject.success(userService.verifylogin(code));
    //}

    // 应用端更新某个loginid的心跳, 5分钟更新一次, 传过来的时间戳是String格式的,要转成long
    @RequestMapping(value="/alive", method = RequestMethod.POST)
    public ResponseObject alive(int loginid, String alive_timestamp){
        userService.alive(loginid, alive_timestamp);
        return ResponseObject.success(String.format("更新loginid: %s 心跳记录",loginid));
    }

    // 管理员平台获取所有用户
    @RequestMapping(value="/findAll", method= RequestMethod.GET)
    public ResponseObject findAll() {
        return ResponseObject.success(userService.findAll());
    }

    // 管理员平台按照名字搜索
    @RequestMapping(value="/findbyNamePage", method= RequestMethod.GET)
    public ResponseObject findbyNamePage(int limit, int page, String name) {
        return ResponseObject.success(userService.findbyNamePage(limit, page, name));
    }

    @RequestMapping(value="/findById/{id}", method= RequestMethod.GET)
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    // 管理员平台创建新用户
    @RequestMapping(value="/createUser", method= RequestMethod.POST)
    public ResponseObject createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseObject.success("成功创建新用户");
    }

    // 管理员平台更新用户信息
    @RequestMapping(value="/updateUser", method= RequestMethod.PUT)
    public ResponseObject updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseObject.success("成功更新新用户");
    }

    // 管理员平台删除用户信息
    @RequestMapping(value="/deleteUser", method= RequestMethod.DELETE)
    public ResponseObject deleteUser(int id) {
        userService.deleteUser(id);
        return ResponseObject.success("成功删除新用户");
    }

}