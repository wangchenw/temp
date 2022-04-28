package com.utils.operatemysql.Exception;

public enum ErrorEnum {
    /*
          错误状态枚举
     */
    //通用错误类型 10000
    NotData(10001, "请求数据不存在"),
    AccountIsDisabled(10002, "该账号已被禁用"),
    ConnectionFailed(10003, "请检查网络连接"),
    MD5_LOADING_ERROR(10004,"加密因子导入错误"),
    //用户相关错误类型 20000
    DUPLICATE_KEY(20001,"唯一键重复"),
    DATA_MISSING(20002,"请求数据缺失"),
    DATA_VALID_ERROR(20003,"请求参数验证失败"),
    CREDENTIAL_VAILD_FAILD(20002,"登录凭证验证失败"),
    //管理员错误类型
    ADMIN_NOT_LOGIN(30001,"管理员账户未登录"),
    SELLER_QUERRY_ERROR(30002, "商户查询异常"),
    SELLER_ADD_ERROR(30003, "商户新增异常");


    private Integer errCode;
    private String errMessage;

    ErrorEnum(Integer errCode, String errMassage) {
        this.errCode = errCode;
        this.errMessage = errMassage;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMassage) {
        this.errMessage = errMassage;
    }
}
