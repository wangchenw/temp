package com.utils.operatemysql.utils;

import lombok.Data;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/3/23 21:54
 */
@Data
public class PageQuery {

    // 页码
    private Integer page = 1;

    // 每页查询条数
    private Integer size = 2;
}
