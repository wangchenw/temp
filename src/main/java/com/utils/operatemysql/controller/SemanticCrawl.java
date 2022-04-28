package com.utils.operatemysql.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/27 16:04
 */
public class SemanticCrawl {



    public static void main(String[] args) {
        Document doc = null;
        try {

            String domain = "albanyairport.com";

             doc = Jsoup.connect("http://"+domain + "/").get();
            // doc = Jsoup.connect("http://ashealthnet.com/").get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String title = doc.title();
        Elements metas = doc.head().select("meta");
        for (Element meta : metas) {
            String content = meta.attr("content");
            if ("keywords".equalsIgnoreCase(meta.attr("name"))) {
                System.out.println("关键字："+content);
            }
            if ("description".equalsIgnoreCase(meta.attr("name"))) {
                System.out.println("网站内容描述:"+content);
            }
        }
        // Elements keywords = doc.getElementsByTag("meta");
        System.out.println("标题"+title);
    }
}