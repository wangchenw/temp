package com.utils.operatemysql.Exception;


import com.utils.operatemysql.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: WangChen
 * @Description: 全局异常处理，捕获并使用统一封装返回
 * @Date: create in 2022/3/22 19:12
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
        自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public Result baseExceptionHandler(BaseException exception) {
        return Result.failed(exception);
    }

    /**
     *
        其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception exception) {
        return Result.failed(500,exception.getMessage());
    }
}
