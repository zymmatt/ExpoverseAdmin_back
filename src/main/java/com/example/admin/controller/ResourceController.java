package com.example.admin.controller;

import com.example.admin.entity.Resource.*;
import com.example.admin.entity.Response.ResponseObject;
import com.example.admin.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    //应用端  当元宇宙前端刚刚加载的时候向后端请求所有的产品的DM图片,电影文件的URL
    @RequestMapping(value="/getAllResource", method= RequestMethod.GET)
    public ResponseObject getAllResource(){
        return ResponseObject.success(resourceService.getAllResource());
    }

    //管理员平台 根据当前的展区id查询产品id列表
    @RequestMapping(value="/getProdListbyExhbid", method= RequestMethod.GET)
    public ResponseObject getProdListbyExhbid(String exhbid){
        return ResponseObject.success(resourceService.getProdListbyExhbid(exhbid));
    }

    //管理员平台 根据当前的产品id查询DM的URL列表用于展示在前端
    @RequestMapping(value="/getDMURLbyProdid", method= RequestMethod.GET)
    public ResponseObject getDMURLbyProdid(String prodid){
        return ResponseObject.success(resourceService.getDMURLbyProdid(prodid));
    }

    //管理员平台 根据当前的展区id查询展区电影的URL列表用于展示在前端
    @RequestMapping(value="/getExhbMovieURLbyExhbid", method= RequestMethod.GET)
    public ResponseObject getExhbMovieURLbyExhbid(String exhbid){
        return ResponseObject.success(resourceService.getExhbMovieURLbyExhbid(exhbid));
    }

    //管理员平台 根据当前的展区id查询产品电影的URL列表用于展示在前端
    @RequestMapping(value="/getProdMovieURLbyExhbid", method= RequestMethod.GET)
    public ResponseObject getProdMovieURLbyExhbid(String exhbid){
        return ResponseObject.success(resourceService.getProdMovieURLbyExhbid(exhbid));
    }

    // 应用端 管理员平台 获得临时的Azure blob storage的SAS来访问URL的缩略图
    @RequestMapping(value="/gettempSAS", method= RequestMethod.GET)
    public ResponseObject gettempSAS(){
        return ResponseObject.success(resourceService.gettempSAS());
    }


    // 管理员平台 上传某一个展区的某一个产品的DM的新排序情况,包括DM的id, name, url
    @RequestMapping(value="/uploadDMdict", method= RequestMethod.POST)
    public ResponseObject uploadDMdict(@RequestBody ProdUpdate prodUpdate) throws IOException {
        resourceService.uploadDMdict(prodUpdate);
        return ResponseObject.success("上传产品DM新排序成功");
    }


    // 管理员平台 更新展区影片
    @RequestMapping(value="/updateExhbMovie", method= RequestMethod.POST)
    public ResponseObject updateExhbMovie(@RequestParam("file") MultipartFile file,
                                  @RequestParam("exhbid") String exhbid,
                                  @RequestParam("name") String name) throws IOException {
        return ResponseObject.success(resourceService.updateExhbMovie(file, exhbid, name));
    }


    // 管理员平台 更新产品影片
    @RequestMapping(value="/updateProdMovie", method= RequestMethod.POST)
    public ResponseObject updateProdMovie(@RequestParam("file") MultipartFile file,
                                  @RequestParam("exhbid") String exhbid,
                                  @RequestParam("name") String name) throws IOException {
        return ResponseObject.success(resourceService.updateExhbMovie(file, exhbid, name));
    }

}
