package com.utils.operatemysql.controller;

import com.utils.operatemysql.service.ZipService;
import com.utils.operatemysql.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/5/6 17:41
 */
@RestController
@AllArgsConstructor
public class FileController {
    private final ZipService zipService;


    @RequestMapping("doZip")
    private Result doZip(@RequestParam String path) {
      return zipService.doZip(path);
    }

    @RequestMapping("nuZip")
    private Result nuZip(@RequestParam String path) {
        return zipService.nuZip(path);
    }

}
