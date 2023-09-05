package com.example.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    // http://localhost:8080/hello
    // http://www.baidu.com/s  协议  域名   路径
    @RequestMapping(value="/hello", method= RequestMethod.GET)
    public String hello(String nickname){
        return "hello java spring"+nickname;

    }

    @RequestMapping(value="/hello2", method= RequestMethod.GET)
    public String hello2(@RequestParam(value="name", required=false) String nickname){
        return "hello java spring"+nickname;

    }

    //@Requestbody   user
}
