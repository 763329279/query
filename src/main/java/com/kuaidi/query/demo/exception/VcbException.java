package com.kuaidi.query.demo.exception;

import com.kuaidi.query.demo.domain.ErrorCode;

/**
 * 基础异常类
 * Created by xuejingtao
 */
public class VcbException extends RuntimeException {

    private int code;
    private String errorCode;


    public VcbException(int code) {
        super();
        this.code = code;
    }
    public VcbException(int code, String message) {
        super(message);
        this.code = code;
        this.errorCode = "";
    }

    public VcbException(String message) {
        super(message);
        this.code = ErrorCode.ERROR_CODE;
    }


    public VcbException(int code, String errorCode, String message) {
        super(message);
        this.code = code;
        this.errorCode = errorCode;
    }

    public int getCode() {
        return code;
    }
}
