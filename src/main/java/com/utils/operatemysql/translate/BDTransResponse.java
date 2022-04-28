package com.utils.operatemysql.translate;

import lombok.Data;

import java.util.List;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/27 18:52
 */
@Data
public class BDTransResponse {
    // 原文语种
    private String from;

    // 目标语种
    private String to;

    // 翻译结果
    private List<BDTransResult> trans_result;

    // 错误编码, 当报错时才有值
    private String error_code;

    // 错误信息, 当报错时才有值
    private String error_msg;

}
