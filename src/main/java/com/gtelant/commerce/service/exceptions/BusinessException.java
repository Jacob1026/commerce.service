package com.gtelant.commerce.service.exceptions;

public class BusinessException  extends RuntimeException{

    private int code;
    //預設500
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
    //自訂錯誤代碼
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }

}
