package com.utils.operatemysql.utils;


import com.utils.operatemysql.Exception.BaseException;
import com.utils.operatemysql.Exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 4307394367499737201L;

    private static final int OK = 0;

    private static final int FAILED = 500;

    private boolean success;

    private int statusCode;

    private String msg = "";

    private T data;

    public static Result ok() {
        return restResult(true, OK, null, null);
    }

    public static<T> Result ok(T data) {
        return restResult(true, OK, null, data);
    }

    public static<T> Result ok(String msg ,T data) {
        return restResult(true, OK, msg, data);
    }



    public static Result failed(String msg) {
        return restResult(false, FAILED, msg, null);
    }

    public static Result failed(BaseException exception) {
        return restResult(false, exception.getErrCode(), exception.getErrMsg(), null);
    }

    public static Result failed(ErrorEnum exception) {
        return restResult(false, exception.getErrCode(), exception.getErrMessage(), null);
    }



    public static Result failed(int statusCode, String msg) {
        return restResult(false, statusCode, msg, null);
    }

    private static<T> Result restResult(boolean success, int statusCode, String msg, T data) {
        return new Result(success, statusCode, msg, data);
    }
}
