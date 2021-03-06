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
 * @description ????????????description?????????????????????Service??????
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
        // ?????????????????????
        // String folderPath = "C:\\Users\\W\\Desktop\\??????\\10012html\\ddd";
        // String folderPath = "C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\10012(1)\\10012\\";
        // String folderPath = "C:\\Users\\W\\Desktop\\???????????????\\";
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
        System.out.println("????????????");

    }

    @Override
    public void insert(String xlsFile) {

        if(!(xlsFile.endsWith(".xls") || xlsFile.endsWith(".xlsx"))){
            log.error("???????????????????????????.xls ??? .xlsx?????????: " + xlsFile);
            throw new RuntimeException("???????????????????????????.xls ??? .xlsx?????????: " + xlsFile);
        }
        try{
            InputStream in = new FileInputStream(xlsFile);
            XSSFWorkbook workbook = new XSSFWorkbook(in);

            //??????????????????????????????
            int sheetNum = workbook.getNumberOfSheets();
            sheetNum = 1;

            //??????????????????
            for(int i = 0; i < sheetNum; i++){
                //????????????????????????

                XSSFSheet sheet = workbook.getSheetAt(i);
                //?????????????????????????????? -- ???????????????
                String sheetName = sheet.getSheetName();
                //????????????????????????????????????
                int rowNum = sheet.getLastRowNum();
                //??????????????? -- ???????????????
                XSSFRow firstRow = sheet.getRow(0);
                //????????????????????????
                int colNum = firstRow.getLastCellNum();


                for(int j = 1; j <= rowNum; j++){
                    Sheet1 sheet1 = new Sheet1();
                    //????????????????????????
                    XSSFRow currRow = sheet.getRow(j);
                    //?????????????????????????????????
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
                                    if (StringUtil.isNotEmpty(cell.getStringCellValue())) {
                                        sheet1.setPort(Integer.valueOf(cell.getStringCellValue()));
                                    }
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
            log.error("?????????????????????: " + xlsFile);
            log.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("??????Excel????????????");
            log.error(e.getMessage());
        }

    }

    private Sheet1 getDescriptionAndtTranslation(Sheet1 sheet1) {
        String screenshot = sheet1.getScreenshot();


        if (screenshot.contains(".")) {
            screenshot = screenshot.substring(0, screenshot.lastIndexOf('.'));
            System.out.println(screenshot);
            // Description url = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot));
            // Description url = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot));
            List<Description> results = descriptionMapper.selectList(new QueryWrapper<Description>().like("url", screenshot));
            if (results != null && results.size()>0) {
                Set<String> collect = results.stream().map(description -> description.getDescription()).collect(Collectors.toSet());
                if (collect.size() > 1) {
                    System.out.println("?????????about???????????????????????????"+collect);
                    //?????????????????????????????????description ?????????????????????about
                    // Description description = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot).notLike("url", "about"));
                    List<Description> descriptions = descriptionMapper.selectList(new QueryWrapper<Description>().like("url", screenshot).notLike("url", "about"));
                    Description description = descriptions.get(0);
                    if (description!=null && StringUtil.isNotEmpty(description.getDescription())) {
                        sheet1.setDescription(description.getDescription());
                        sheet1.setTranslation(en2ch(description.getDescription()));
                    }
                }else {

                    sheet1.setDescription(collect.stream().collect(Collectors.toList()).get(0));
                    sheet1.setTranslation(en2ch(collect.stream().collect(Collectors.toList()).get(0)));
                }
        }



        }
        // ????????? screenshot
        // if (screenshot.contains("\\")){
        //     screenshot = screenshot.substring(screenshot.indexOf('\\')+1, screenshot.lastIndexOf('.'));
        //     if (screenshot.contains("_")) {
        //         screenshot = screenshot.substring(0, screenshot.lastIndexOf('_'));
        //     }
        //
        //     System.out.println(screenshot);
        //
        //     // Description url = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot));
        //     // Description url = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot));
        //     List<Description> results = descriptionMapper.selectList(new QueryWrapper<Description>().like("url", screenshot));
        //     if (results != null && results.size()>0) {
        //         Set<String> collect = results.stream().map(description -> description.getDescription()).collect(Collectors.toSet());
        //         if (collect.size() > 1) {
        //             System.out.println("?????????about???????????????????????????"+collect);
        //             //?????????????????????????????????description ?????????????????????about
        //             // Description description = descriptionMapper.selectOne(new QueryWrapper<Description>().like("url", screenshot).notLike("url", "about"));
        //             List<Description> descriptions = descriptionMapper.selectList(new QueryWrapper<Description>().like("url", screenshot).notLike("url", "about"));
        //             Description description = descriptions.get(0);
        //             if (description!=null && StringUtil.isNotEmpty(description.getDescription())) {
        //                 sheet1.setDescription(description.getDescription());
        //             }
        //         }else {
        //             sheet1.setDescription(collect.stream().collect(Collectors.toList()).get(0));
        //         }
        //
        //     }
        // }
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
                        //???????????????
                        extractMsg.setKeyword(content);
                }
                if ("description".equalsIgnoreCase(meta.attr("name"))) {
                    //??????????????????
                    //???????????????????????????????????????????????????
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
                    replace = replace.trim(); //??????????????????
                    if (replace.length() >= content.length() && replace.length() > 30) {
                        extractMsg.setDescription(replace);
                    }
                    if (content.length() > replace.length() && content.length() > 30) {
                        extractMsg.setDescription(content);
                    }
                  }             }
            //????????????
            extractMsg.setUrl(domain);
            /**
             *  ??? meta ????????????description????????????p???????????????
             */
            if (StringUtil.isEmpty(extractMsg.getDescription())) {
                //?????????p???????????????
                Elements mastheads = doc.select("p");
                for (Element element : mastheads) {
                    String text = element.text();
                    for (String keyword : keywords) {
                        if (StringUtil.isEmpty(extractMsg.getDescription())) {
                            int i = ignoreCaseIndexOf(text, keyword);
                            if (i!=-1){
                                if (text.length() > 30) {
                                    System.out.println("????????????:"+text);
                                    System.out.println("???????????????:"+keyword);
                                    extractMsg.setDescription(text);
                                    extractMsg.setSearch(keyword);
                                }
                            }
                        }
                    }
                }
            }
            /**
             *  ????????????div??????????????????????????????????????????????????????text????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????list???String??????????????????
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
                    extractMsg.setSearch("????????????????????????");
                }
            }

            /*
               ???????????? ?????????????????? ??????????????? ??????
             */
            // if (StringUtil.isEmpty(extractMsg.getDescription())) {
            //     String text = doc.text();
            //     // String regEx="[??????????.!]";
            //     String regEx="[.]";
            //     Pattern p =Pattern.compile(regEx);
            //     /*?????????????????????????????????*/
            //     String[] substrs = p.split(text);
            //     for (String substr : substrs) {
            //         for (String keyword : keywords) {
            //             if (substr.contains(keyword)) {
            //                 // System.out.println(substr);
            //                 if (substr.length() > 20) {
            //                     System.out.println("????????????:"+substr);
            //                     System.out.println("???????????????:"+keyword);
            //                     extractMsg.setDescription(substr);
            //                     extractMsg.setSearch(keyword);
            //                 }
            //             }
            //         }
            //     }
            // }

            /**
             *  ?????????????????????????????????html?????????????????????????????????
             */
            if (StringUtil.isEmpty(extractMsg.getDescription())) {

                // ??????HTML?????????
                StringBuffer htmlSb = new StringBuffer();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            new FileInputStream(input), "utf-8"));
                    while (br.ready()) {
                        String singleline = br.readLine();
                        htmlSb.append(singleline);
                        //????????????????????????description
                        if (singleline.matches("(.*)description.(.*)")) {
                            //???description??????

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
                    // ??????????????????
                    //file.delete();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        /**
         *    ??????p??????????????????????????????????????????????????????nofound??????
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




