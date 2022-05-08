package com.utils.operatemysql.service;

import com.utils.operatemysql.utils.Result;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/5/6 17:44
 */
public interface ZipService {
    Result doZip(String path);

    Result nuZip(String path);
}
