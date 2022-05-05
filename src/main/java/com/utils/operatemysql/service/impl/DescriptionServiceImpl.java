package com.utils.operatemysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.utils.operatemysql.entity.Description;
import com.utils.operatemysql.entity.Nofound;
import com.utils.operatemysql.entity.Sheet1;
import com.utils.operatemysql.mapper.NofoundMapper;
import com.utils.operatemysql.mapper.Sheet1Mapper;
import com.utils.operatemysql.service.DescriptionService;
import com.utils.operatemysql.mapper.DescriptionMapper;
import com.utils.operatemysql.service.NofoundService;
import com.utils.operatemysql.utils.ElementUtils;
import com.utils.operatemysql.utils.Result;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.jsoup.nodes.DataNode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.utils.operatemysql.translate.BDTransApiUtil.en2ch;
import static com.utils.operatemysql.utils.StringUtil.*;

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
    private final Sheet1Mapper sheet1Mapper;

    private static Pattern p = Pattern.compile("(?<=content=\").*?(?=\")");


    @Override
    public void spiderMysql(String folderPath) throws IOException {
        // extract("C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012\\circuitcheck.com_80.html");
        // 首先遍历文件夹
        // String folderPath = "C:\\Users\\W\\Desktop\\分类\\10012html\\ddd";
        // String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012\\";
        // String folderPath = "C:\\Users\\W\\Desktop\\新建文件夹\\";
        // String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012\\";
        // String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\trouble\\";
        // String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\res(1)\\res\\";
        if (!folderPath.endsWith("\\")) {
            folderPath = folderPath + "\\";
        }
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

    @Override
    public void insert(String xlsFile) {

        if(!(xlsFile.endsWith(".xls") || xlsFile.endsWith(".xlsx"))){
            log.error("文件格式错误，应为.xls 或 .xlsx，你的: " + xlsFile);
            throw new RuntimeException("文件格式错误，应为.xls 或 .xlsx，你的: " + xlsFile);
        }
        try{
            InputStream in = new FileInputStream(xlsFile);
            XSSFWorkbook workbook = new XSSFWorkbook(in);

            //获取工作簿中表的数量
            int sheetNum = workbook.getNumberOfSheets();
            sheetNum = 1;

            //遍历每一张表
            for(int i = 0; i < sheetNum; i++){
                //获取当前操作的表

                XSSFSheet sheet = workbook.getSheetAt(i);
                //获取当前操作表的名称 -- 数据库表名
                String sheetName = sheet.getSheetName();
                //获取当前操作表的数据行数
                int rowNum = sheet.getLastRowNum();
                //获取第一行 -- 表中的列名
                XSSFRow firstRow = sheet.getRow(0);
                //获取每一行的列数
                int colNum = firstRow.getLastCellNum();


                for(int j = 1; j <= rowNum; j++){
                    Sheet1 sheet1 = new Sheet1();
                    //获取当前操作的行
                    XSSFRow currRow = sheet.getRow(j);
                    //遍历当前操作行的每一列
                    // StringBuffer values = new StringBuffer();
                    for(int col = 1; col < colNum; col++){
                        XSSFCell cell = currRow.getCell(col);
                        if (cell != null) {
                            cell.setCellType(CellType.STRING);
                            switch(col){
                                case 1:
                                    sheet1.setCharacteristic(cell.getStringCellValue());
                                    break;
                                case 2:
                                    sheet1.setPlatform(cell.getStringCellValue());
                                    break;
                                case 3:
                                    sheet1.setIp(cell.getStringCellValue());
                                    break;
                                case 4:
                                    sheet1.setPort(Integer.valueOf(cell.getStringCellValue()));
                                    break;
                                case 5:
                                    sheet1.setProperty(cell.getStringCellValue());
                                    break;
                                case 6:
                                    sheet1.setCountry(cell.getStringCellValue());
                                    break;
                                case 7:
                                    sheet1.setAsMassage(cell.getStringCellValue());
                                    break;
                                case 8:
                                    sheet1.setWhoisMessage(cell.getStringCellValue());
                                    break;
                                case 9:
                                    sheet1.setIpuser(cell.getStringCellValue());
                                    break;
                                case 10:
                                    sheet1.setCertificationUrl(cell.getStringCellValue());
                                    break;
                                case 11:
                                    sheet1.setScreenshot(cell.getStringCellValue());
                                    break;
                                case 12:
                                    sheet1.setCharacteristic(cell.getStringCellValue());
                                    break;
                                case 13:
                                    sheet1.setDescription(cell.getStringCellValue());
                                    break;
                                case 15:
                                    sheet1.setTranslation(cell.getStringCellValue());
                                    break;
                                case 14:
                                    sheet1.setVersion(cell.getStringCellValue());
                                    break;
                                default :
                                    System.out.println("hello");
                                    break;
                            }
                        }

                    }
                    if (StringUtil.isNotEmpty(sheet1.getScreenshot())) {
                        sheet1 = getDescriptionAndtTranslation(sheet1);
                    }
                    sheet1Mapper.insert(sheet1);

                }
            }


        } catch (FileNotFoundException e){
            log.error("找不到目标文件: " + xlsFile);
            log.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("加载Excel文件失败");
            log.error(e.getMessage());
        }

    }

    private Sheet1 getDescriptionAndtTranslation(Sheet1 sheet1) {
        String screenshot = sheet1.getScreenshot();
        // 先处理 screenshot
        if (screenshot.contains("\\")){
            screenshot = screenshot.substring(screenshot.indexOf('\\')+1, screenshot.lastIndexOf('.'));
            if (screenshot.contains("_")) {
                screenshot = screenshot.substring(0, screenshot.lastIndexOf('_'));
            }

            System.out.println(screenshot);

            // Description url = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot));
            // Description url = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot));
            List<Description> results = descriptionMapper.selectList(new QueryWrapper<Description>().like("url", screenshot));
            if (results != null && results.size()>0) {
                Set<String> collect = results.stream().map(description -> description.getDescription()).collect(Collectors.toSet());
                if (collect.size() > 1) {
                    System.out.println("主页和about页面采集信息不一致"+collect);
                    //还是获取主页的介绍作为description 不要拼，优先拿about
                    Description description = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot).notLike("url", "about"));
                    if (description!=null && StringUtil.isNotEmpty(description.getDescription())) {
                        sheet1.setDescription(description.getDescription());
                    }
                }else {
                    sheet1.setDescription(collect.stream().collect(Collectors.toList()).get(0));
                }

            }
        }
        return sheet1;
    }


    private void extract(String domain,String path) throws IOException {
        String[] keywords = {" mission ","welcome to "," is a "," is an ","We are a","We are an","We solve","focus on"};
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
                    StringBuffer description = new StringBuffer();
                    for (Attribute attribute : meta.attributes()) {

                        String key = attribute.getKey();
                        if (!key.equals("name")&&!key.equals("lang")&&!key.equals("id")) {
                            if (StringUtil.isNotEmpty(attribute.getValue())) {
                                description.append(" "+attribute.getValue());
                            }
                            if (StringUtil.isEmpty(attribute.getValue())) {
                                description.append(" "+key);
                            }
                        }
                    }
                    String replace = description.toString().replace("\"", "");
                    replace = replace.trim(); //去掉多余空格
                    if (replace.length() >= content.length() && replace.length() > 30) {
                        extractMsg.setDescription(replace);
                    }
                    if (content.length() > replace.length() && content.length() > 30) {
                        extractMsg.setDescription(content);
                    }
                  }             }
            //存入标题
            extractMsg.setUrl(domain);
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
                                if (text.length() > 30) {
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
            /**
             *  获取每段div文本，可惜的是我不能直接获取最里层的text文本，但是我可以嵌套获取每一层数据，问题是最外层数据完全包含了内层数据，我为了的到最终的匹配结果，直接对list中String取长度最小的
             */
            if (StringUtil.isEmpty(extractMsg.getDescription())) {
                Elements allElements = doc.getAllElements();
                List<String> strings = allElements.eachText();
                Set<String> texts = new HashSet<>(strings);
                List<String> foundtext = new ArrayList<>();
                for (String text : texts) {
                    for (String keyword : keywords) {
                        if (text.contains(keyword)) {
                            foundtext.add(text);
                        }
                    }
                }
                String minValue = getMinValue(foundtext);
                if (minValue.length() > 30) {
                    extractMsg.setDescription(minValue);
                    extractMsg.setSearch("由关键词模糊匹配");
                }
            }

            /*
               手动截取 并使用通配符 截取每一句 句子
             */
            // if (StringUtil.isEmpty(extractMsg.getDescription())) {
            //     String text = doc.text();
            //     // String regEx="[。？！?.!]";
            //     String regEx="[.]";
            //     Pattern p =Pattern.compile(regEx);
            //     /*按照句子结束符分割句子*/
            //     String[] substrs = p.split(text);
            //     for (String substr : substrs) {
            //         for (String keyword : keywords) {
            //             if (substr.contains(keyword)) {
            //                 // System.out.println(substr);
            //                 if (substr.length() > 20) {
            //                     System.out.println("匹配成功:"+substr);
            //                     System.out.println("匹配关键词:"+keyword);
            //                     extractMsg.setDescription(substr);
            //                     extractMsg.setSearch(keyword);
            //                 }
            //             }
            //         }
            //     }
            // }

            /**
             *  找不到信息记录，直接将html作为文件输入流进行匹配
             */
            if (StringUtil.isEmpty(extractMsg.getDescription())) {

                // 获取HTML文件流
                StringBuffer htmlSb = new StringBuffer();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            new FileInputStream(input), "utf-8"));
                    while (br.ready()) {
                        String singleline = br.readLine();
                        htmlSb.append(singleline);
                        //如果每一行中包含description
                        if (singleline.matches("(.*)description.(.*)")) {
                            //取description的值

                            Matcher matcher = p.matcher(singleline);
                            if( matcher.find() )
                            {
                                String foundstring = matcher.group();
                                if (foundstring.length() > 30) {
                                    extractMsg.setDescription(foundstring);
                                }

                            }
                        }

                    }
                    br.close();
                    // 删除临时文件
                    //file.delete();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        /**
         *    假设p标签里也找不到信息，将错误信息存放在nofound表中
         */

        if (StringUtil.isEmpty(extractMsg.getDescription())) {
            Nofound nofound = new Nofound();
            nofound.setUrl(domain);
            Elements mastheads = doc.select("p");
            for (Element element : mastheads) {
                nofound.setText(element.text());
                nofoundService.save(nofound);
            }
        }else{

            // extractMsg.setTranslation(en2ch(extractMsg.getDescription()));
            descriptionMapper.insert(extractMsg);
        }
    }










}




