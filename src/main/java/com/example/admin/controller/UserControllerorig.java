// UserController.java
package com.example.admin.controller;

import com.example.admin.entity.Response.ResponseObject;
import com.example.admin.entity.User.Login;
import com.example.admin.entity.User.User;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/users")
public class UserControllerorig {
    @Autowired
    private UserService userService;

    // 演示时使用的临时登录,不用查询邀请码,而是返回最近注册的用户的信息
    @RequestMapping(value="/templogin", method= RequestMethod.GET)
    public Login login2(String code) {
        return userService.templogin(code);
    }

    // 应用端输入验证码完成登录
    @RequestMapping(value="/login", method= RequestMethod.GET)
    public ResponseObject login(String code) {
        // return userService.verifylogin(code);
        Login login = userService.verifylogin(code);
        if (login == null){
            return ResponseObject.fail(login,"验证码错误");
        }
        else if (login.getUserid()==-1){
            return ResponseObject.fail(login, "验证码的访问时间还没到");
        }
        else if (login.getUserid()==-2){
            return ResponseObject.fail(login, "验证码的访问时间已经过期");
        }
        else {
            return ResponseObject.success(login);
        }
    }

    // public ResponseObject login(String code) {
    //    return ResponseObject.success(userService.verifylogin(code));
    //}

    // 应用端更新某个loginid的心跳, 5分钟更新一次, 传过来的时间戳是String格式的,要转成long,
    // 同时会检验是否有异常登录的情况,如果心跳的loginid发生了变化,就说明有其他人登录了
    @RequestMapping(value="/alive", method = RequestMethod.POST)
    public ResponseObject alive(int loginid, String alive_timestamp){
        int newloginid = userService.alive(loginid, alive_timestamp);
        return ResponseObject.success(newloginid,String.format("更新loginid: %s 心跳记录",newloginid));
    }

    // 应用端登录时请求语言资料
    @RequestMapping(value="/language", method = RequestMethod.GET)
    public String getLanguage() throws IOException {
        return userService.getLanguage();
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

    // 管理员后台  首页根据时间区间获得这段时间内的参观人数(按天统计)
    @RequestMapping(value="/getUserbyDate", method= RequestMethod.GET)
    public ResponseObject getUserbyDate(String startDate, String endDate){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long startday = sdf.parse(startDate).getTime()/1000;
            long endday = sdf.parse(endDate).getTime()/1000;
            // endday = endday+86400; // 终止日要加24小时,包含进当天
            return ResponseObject.success(userService.getUserbyDate(startday, endday));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 管理员后台  首页根据时间区间获得这段时间内的参观人次(按天统计)
    @RequestMapping(value="/getVisitbyDate", method= RequestMethod.GET)
    public ResponseObject getVisitbyDate(String startDate, String endDate){
        long startday = Long.parseLong(startDate);
        long endday =  Long.parseLong(endDate);
        // endday = endday+86400; // 终止日要加24小时,包含进当天
        return ResponseObject.success(userService.getVisitbyDate(startday, endday));
    }

    // 管理员后台  首页根据时间区间获得这段时间内的新注册人次(按天统计)
    @RequestMapping(value="/getNewUserbyDate", method= RequestMethod.GET)
    public ResponseObject getNewUserbyDate(String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println(startDate);
        //System.out.println(endDate);
        String startday = sdf.format(new Date(1000*Long.parseLong(startDate)));
        String endday = sdf.format(new Date(1000*Long.parseLong(endDate)));
        //System.out.println("line111");
        //System.out.println(startday);
        //System.out.println(endday);
        // endday = endday+86400; // 终止日要加24小时,包含进当天
        return ResponseObject.success(userService.getNewUserbyDate(startday, endday));

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