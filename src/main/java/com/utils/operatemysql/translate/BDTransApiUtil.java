package com.utils.operatemysql.translate;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/27 18:53
 */
public class BDTransApiUtil {
    // 百度翻译地址
    private static final String TRANS_API_HOST = "https://api.fanyi.baidu.com/api/trans/vip/translate";

    // 百度翻译注册应用id
    private static final String APPID = "20200705000513589";

    // 百度翻译注册秘钥
    private static final String SECURITYKEY = "8PAv67N7O2Ju8hUN2XdH";



    /**
     * @Description 中文翻译成中文
     * @param chineseQuery 中文
     * @return 英文
     * @author zongf
     * @date 2018年12月7日-下午1:35:31
     */
    public static String  en2ch(String chineseQuery){

        // 随机数
        long salt = System.currentTimeMillis();

        // 签名
        String src = APPID + chineseQuery + salt + SECURITYKEY; // 加密前的原文
        String sign = DigestUtils.md5DigestAsHex(src.getBytes());


        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("from", "en");
        requestMap.put("to", "zh");
        requestMap.put("appid", APPID);
        requestMap.put("salt","" + salt);
        requestMap.put("q", chineseQuery);
        requestMap.put("sign", sign);

        // 调用翻译API
        String result = HttpUtil.get(TRANS_API_HOST, requestMap);
        // 解析响应结果
        BDTransResponse response = JSONObject.parseObject(result, BDTransResponse.class);

        // 判断错误码
        if(response.getError_code() != null){
            return "翻译报错-错误码:" + response.getError_code() + ",错误原因:" + response.getError_msg();
        }

        // 返回结果
        List<BDTransResult> transResults = response.getTrans_result();

        if(null != transResults && transResults.size() > 0){
            return transResults.get(0).getDst();
        }

        return null;
    }

}
