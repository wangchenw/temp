package com.utils.operatemysql.service.impl;

import com.utils.operatemysql.Exception.BaseException;
import com.utils.operatemysql.service.ZipService;
import com.utils.operatemysql.utils.Result;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/5/6 17:45
 */
@Service
public class ZipServiceImpl implements ZipService {
    @Override
    public Result doZip(String path) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            return Result.failed("文件地址不存在");
        }
        if (pathFile.isDirectory()) {
            try {
                RecursiveZip(pathFile);
            } catch (ZipException e) {
                throw new BaseException("文件夹压缩失败");
            }
        }else{
            try {
                SingleZip(pathFile);
            } catch (ZipException e) {
                throw new BaseException("文件压缩失败");
            }
        }

        return Result.ok("文件压缩成功");
    }

    @Override
    public Result nuZip(String path) {
        File file = new File(path);

        String password = getFilePassword(file);
        try {
            new ZipFile( file.getName(), password.toCharArray()).extractAll(file.getParent());
        } catch (ZipException e) {
            throw new BaseException("文件解压缩失败");
        }
        return Result.ok("文件解压成功");
    }

    public String getFilePassword(File pathFile) {
        if (pathFile != null) {
            Path path = pathFile.toPath();
            try {
                FileTime fileTime = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                return formatter.format(fileTime.toMillis())+"!!!@@@";
            } catch (IOException e) {
                 throw new BaseException("压缩包创建时间解析失败");
            }
        }else {
            throw new BaseException("压缩包不存在");
        }

    }

    private void RecursiveZip(File pathFile) throws ZipException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);


        new ZipFile(pathFile.getName()+".zip",getPassword().toCharArray()).addFolder(pathFile,zipParameters);
    }

    private void SingleZip(File pathFile) throws ZipException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        System.out.println(pathFile.getName());
        new ZipFile(pathFile.getName()+".zip",getPassword().toCharArray()).addFile(pathFile,zipParameters);
    }



    public static String getPassword() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date)+"!!!@@@";
    }

}
