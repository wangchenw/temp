package com.utils.operatemysql.Exception;

import lombok.Data;


@Data
public class BaseException extends RuntimeException {

    private int errCode;

    private String errMsg;

    public BaseException() {
    }

    public BaseException(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BaseException(ErrorEnum errorEnum) {
        this.errCode = errorEnum.getErrCode();
        this.errMsg = errorEnum.getErrMessage();
    }

    public BaseException(ErrorEnum errorEnum,String message) {
        this.errCode = errorEnum.getErrCode();
        this.errMsg = message;
    }

    public BaseException( String errMsg) {
        this.errCode = 500;
        this.errMsg = errMsg;
    }
}
