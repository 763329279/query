package com.kuaidi.query.demo.exception;

/**
 * Created by xuejingtao on 2018/1/24.
 */
public class BusinessException extends VcbException {

    public BusinessException(int code) {
        super(code);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }
}
