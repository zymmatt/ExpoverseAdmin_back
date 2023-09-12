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


    //根据当前的展区id查询产品id列表
    @RequestMapping(value="/getProdListbyExhbid", method= RequestMethod.GET)
    public List<product> getProdListbyExhbid(String exhbid){
        return resourceService.getProdListbyExhbid(exhbid);
    }

    //根据当前的产品id查询DM的URL列表用于展示在前端
    @RequestMapping(value="/getDMURLbyProdid", method= RequestMethod.GET)
    public List<DM> getDMURLbyProdid(String prodid){
        return resourceService.getDMURLbyProdid(prodid);
    }

    // 获得临时的Azure blob storage的SAS来访问URL的缩略图
    @RequestMapping(value="/gettempSAS", method= RequestMethod.GET)
    public String gettempSAS(){
        return resourceService.gettempSAS();
    }
}
