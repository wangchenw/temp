package com.utils.operatemysql.service;

import com.utils.operatemysql.entity.Description;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.operatemysql.utils.Result;

import java.io.IOException;

/**
* @author W
* @description 针对表【description】的数据库操作Service
* @createDate 2022-04-27 17:00:29
*/
public interface DescriptionService extends IService<Description> {

    void spiderMysql() throws IOException;
}
