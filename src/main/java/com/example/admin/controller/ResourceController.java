package com.example.admin.controller;

import com.example.admin.entity.Resource.product;
import com.example.admin.entity.Resource.DM;

import com.example.admin.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    //当元宇宙前端刚刚加载的时候向后端请求所有的产品的DM图片排列顺序
    @RequestMapping(value="/getAllProduct", method= RequestMethod.GET)
    public List<product> getAllProduct(){
        return resourceService.getAllProduct();
    }





}
