package com.utils.operatemysql.translate;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/27 18:52
 */
@Data
public class BDTransRequest {
    // 翻译源语种, 默认为中文
    private String from = "zh";

    // 目标语种, 默认为英文
    private String to = "en";

    // 百度翻译平台注册id
    private String appid;

    // 随机数
    private String salt;

    // 翻译原文, UTF-8编码
    private String q;

    // 数字签名=md5(appid+q+salt+密钥)
    private String sign;


}
