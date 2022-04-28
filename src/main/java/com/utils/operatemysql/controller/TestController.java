package com.utils.operatemysql.controller;

import com.utils.operatemysql.service.DescriptionService;
import com.utils.operatemysql.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/2 16:43
 */

@RestController
@AllArgsConstructor
public class TestController {
    private final DescriptionService descriptionService;

    //解析json文件存储MySQL数据库
    @RequestMapping("spider")
    public void saveMysql() throws IOException {
        descriptionService.spiderMysql();
    }



}
