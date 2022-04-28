package com.utils.operatemysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.utils.operatemysql.entity.Description;
import com.utils.operatemysql.entity.Nofound;
import com.utils.operatemysql.mapper.NofoundMapper;
import com.utils.operatemysql.service.DescriptionService;
import com.utils.operatemysql.mapper.DescriptionMapper;
import com.utils.operatemysql.service.NofoundService;
import com.utils.operatemysql.utils.Result;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.utils.operatemysql.translate.BDTransApiUtil.en2ch;
import static com.utils.operatemysql.utils.StringUtil.ignoreCaseIndexOf;

/**
 * @author W
 * @description 针对表【description】的数据库操作Service实现
 * @createDate 2022-04-27 17:00:29
 */
@Service
@AllArgsConstructor
public class DescriptionServiceImpl extends ServiceImpl<DescriptionMapper, Description>
        implements DescriptionService {
    private final DescriptionMapper descriptionMapper;
    private final NofoundService nofoundService;

    @Override
    public void spiderMysql() throws IOException {
        // extract("C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012\\circuitcheck.com_80.html");
        // 首先遍历文件夹
        // String folderPath = "C:\\Users\\W\\Desktop\\分类\\10012html\\ddd";
        String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012\\";
        File dir = new File(folderPath);

        Map<String, String> domainlist = new HashMap<>();
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith("_80.html")) {
                String name = file.getName();
                // System.out.println(name.substring(0, name.lastIndexOf('_')));
                domainlist.put(name.substring(0, name.lastIndexOf('_')), folderPath+name );
            }
        }
        domainlist.forEach((domain,path)->{
            try {
                domain = "http://" + domain + "/";
                extract(domain, path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("检测完毕");

    }

    private void extract(String domain,String path) throws IOException {
        String[] keywords = {" mission ","welcome to "," is a "," is an "," we are a "," we are an "," we solve "," we solve "};
        Description extractMsg = new Description();
        Document doc = null;
        File input = new File(path);
        doc = Jsoup.parse(input,"UTF-8", domain);

        if (doc != null) {
            String title = doc.title();
            extractMsg.setTitle(title);
            Elements metas = doc.head().select("meta");
            for (Element meta : metas) {
                String content = meta.attr("content");
                if ("keywords".equalsIgnoreCase(meta.attr("name"))) {


                        //存入关键字
                        extractMsg.setKeyword(content);

                }
                if ("description".equalsIgnoreCase(meta.attr("name"))) {
                    //存入网站描述
                    //加入字符长度限制，字符长度太短不行
                    if (content.length() > 20) {
                        extractMsg.setDescription(content);
                    }

                    //暂时先不翻
                    // extractMsg.setTranslation(en2ch(content));
                    // System.out.println("网站内容描述:"+content);

                }
            }
            //存入标题
            extractMsg.setUrl(domain);
            // Elements keywords = doc.getElementsByTag("meta");

            /**
             *  当 meta 中不包含description信息，从p标签中获取
             */
            if (StringUtil.isEmpty(extractMsg.getDescription())) {
                //尝试从p标签中获取
                Elements mastheads = doc.select("p");
                for (Element element : mastheads) {
                    String text = element.text();
                    for (String keyword : keywords) {
                        if (StringUtil.isEmpty(extractMsg.getDescription())) {
                            int i = ignoreCaseIndexOf(text, keyword);
                            if (i!=-1){
                                if (text.length() > 20) {
                                    System.out.println("匹配成功:"+text);
                                    System.out.println("匹配关键词:"+keyword);
                                    extractMsg.setDescription(text);
                                    extractMsg.setSearch(keyword);
                                }
                            }
                        }
                    }
                }
            }
            //假设p标签里也找不到信息，将错误信息存放在nofound表中
            if (StringUtil.isEmpty(extractMsg.getDescription())) {
                Nofound nofound = new Nofound();
                nofound.setUrl(domain);
                Elements mastheads = doc.select("p");
                for (Element element : mastheads) {
                    nofound.setText(element.text());
                    nofoundService.save(nofound);
                }
            }else{
                descriptionMapper.insert(extractMsg);
            }
        }

    }


    // public void spiderMysql() {
    //     //首先遍历文件夹
    //     // String folderPath = "C:\\Users\\W\\Desktop\\分类\\10012html\\ddd";
    //     String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012";
    //     File dir = new File(folderPath);
    //     List<String> domainlist = new ArrayList<>();
    //     for (File file : dir.listFiles()) {
    //         if (file.isFile() && file.getName().endsWith("_80.html")) {
    //             String name = file.getName();
    //             // System.out.println(name.substring(0, name.lastIndexOf('_')));
    //             domainlist.add(name.substring(0, name.lastIndexOf('_')));
    //         }
    //     }
    //     for (String domain : domainlist) {
    //         extractSourceCode(domain);
    //     }
    //
    //     System.out.println("检索完毕");
    //
    // }

    private void extractSourceCode(String domain) {
        String[] keywords = {"mission", "description","describe","keyword","welcome","we are","about us"};

        Description extractMsg = new Description();
        Document doc = null;
        String url = "http://" + domain + "/";

        try {
            doc = Jsoup.connect(url).get();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            // 这个问题不大，是我网络的问题，后续不是自己连接url 取得网络源代码
            System.out.println("该url无法访问："+url);
        }
        if (doc != null) {
            String title = doc.title();
            extractMsg.setTitle(title);
            Elements metas = doc.head().select("meta");
            for (Element meta : metas) {
                String content = meta.attr("content");
                if ("keywords".equalsIgnoreCase(meta.attr("name"))) {
                    //存入关键字
                    extractMsg.setKeyword(content);
                }
                if ("description".equalsIgnoreCase(meta.attr("name"))) {
                    //存入网站描述
                    extractMsg.setDescription(content);
                    //暂时先不翻
                    // extractMsg.setTranslation(en2ch(content));
                    // System.out.println("网站内容描述:"+content);

                }
            }
            //存入标题
            extractMsg.setUrl(url);
            // Elements keywords = doc.getElementsByTag("meta");

            /**
             *  当 meta 中不包含description信息，从p标签中获取
             */
            if (StringUtil.isEmpty(extractMsg.getDescription())) {
                //尝试从p标签中获取
                Elements mastheads = doc.select("p");
                for (Element element : mastheads) {
                    String text = element.text();
                    for (String keyword : keywords) {
                        int i = ignoreCaseIndexOf(text, keyword);
                        if (i!=-1){
                            System.out.println("匹配成功:"+text);
                            System.out.println("匹配关键词:"+keyword);
                            extractMsg.setDescription(text);
                            extractMsg.setSearch(keyword);
                        }
                    }

                }
            }


            //假设p标签里也找不到信息，将错误信息存放在nofound表中
            if (StringUtil.isEmpty(extractMsg.getDescription())) {
                Nofound nofound = new Nofound();
                nofound.setUrl(url);
                Elements mastheads = doc.select("p");
                for (Element element : mastheads) {
                    nofound.setText(element.text());
                    nofoundService.save(nofound);
                }
            }else{
                descriptionMapper.insert(extractMsg);
            }
        }

    }

}




