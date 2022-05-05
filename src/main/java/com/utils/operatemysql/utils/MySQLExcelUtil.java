package com.utils.operatemysql.utils;

import com.utils.operatemysql.entity.Sheet1;
import com.utils.operatemysql.mapper.Sheet1Mapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WangChen
 * @Description: 将MySQL中的数据导出到Excel表格中，或将Excel数据传入mysql
 * @Date: create in 2022/4/29 19:22
 */

@Slf4j
public class MySQLExcelUtil {

    // public static void main(String[] args) {
    //     importFromExcelToMySQL("C:\\Users\\W\\Documents\\WeChat Files\\wxid_dd2l39sslfud22\\FileStorage\\File\\2022-04\\747v0.2\\747v0.2.xlsx");
    // }

    @Autowired
    private Sheet1Mapper sheet1Mapper;

    public void importFromExcelToMySQL(String xlsFile) {


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
                    //获取当前操作的行
                    XSSFRow currRow = sheet.getRow(j);
                    //遍历当前操作行的每一列
                    // StringBuffer values = new StringBuffer();
                    for(int col = 1; col < colNum; col++){
                        XSSFCell cell = currRow.getCell(col);
                        cell.setCellType(CellType.STRING);
                        Sheet1 sheet1 = new Sheet1();
                        switch(col){
                            case 1:
                                sheet1.setCharacteristic(cell.getStringCellValue());  break;
                            case 2:
                                sheet1.setPlatform(cell.getStringCellValue());  break;
                            case 3:
                                sheet1.setIp(cell.getStringCellValue());  break;
                            case 4:
                                sheet1.setPort(Integer.valueOf(cell.getStringCellValue()));  break;
                            case 5:
                                sheet1.setCountry(cell.getStringCellValue());  break;
                            case 6:
                                sheet1.setAsMassage(cell.getStringCellValue());  break;
                            case 7:
                                sheet1.setWhoisMessage(cell.getStringCellValue());  break;
                            case 8:
                                sheet1.setIpuser(cell.getStringCellValue());  break;
                            case 9:
                                sheet1.setCertificationUrl(cell.getStringCellValue());  break;
                            case 10:
                                sheet1.setScreenshot(cell.getStringCellValue());  break;
                            case 11:
                                sheet1.setCharacteristic(cell.getStringCellValue());  break;
                            case 12:
                                sheet1.setDescription(cell.getStringCellValue());  break;
                            case 13:
                                sheet1.setTranslation(cell.getStringCellValue());  break;
                            default:
                                sheet1.setVersion(cell.getStringCellValue());  break;
                        }
                        sheet1Mapper.insert(sheet1);
                    }


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

}
