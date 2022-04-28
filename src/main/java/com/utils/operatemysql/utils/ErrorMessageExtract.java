package com.utils.operatemysql.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/3/22 22:18
 */
public class ErrorMessageExtract {

    public static String extractErrorMessage(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError allError : bindingResult.getAllErrors()) {
                stringBuilder.append(allError.getDefaultMessage()+",");
            }
            return stringBuilder.substring(0, stringBuilder.toString().length()-1);
        }
        else {
            return "";
        }

    }
}
