package com.utils.operatemysql.controller;

import com.utils.operatemysql.service.DescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void saveMysql(@RequestParam String path) throws IOException {
        descriptionService.spiderMysql(path);
    }

    @RequestMapping("insert")
    public void insertMysql(String s) {
        // descriptionService.insert("C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\747v0.2\\747v0.2.xlsx");
        descriptionService.insert("C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-05\\pcs(2)\\IP使用者分析报告-PulseSecure-SSL-VPN-20211119-V1.xlsx");
        // new MySQLExcelUtil().importFromExcelToMySQL("C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\747v0.2\\747v0.2.xlsx");

    }


}
