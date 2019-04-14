package com.kuaidi.query.demo.domain;

/**
 * Created by Administrator on 2015/4/29.
 */
public class ErrorCode {
    //成功
    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "SUCCESS";

    /**系统错误码：5xx，记录系统级别错误，如系统超时等**/
    //失败
    public static final int ERROR_CODE = 500;
    public static final String ERROR_MSG = "服务器内部错误，请重试或联系客服";//

    public static final int VERSION_IS_UPGRADED = 999;

    public static JsonResult success() {
        return new JsonResult(ErrorCode.SUCCESS_CODE, ErrorCode.SUCCESS_MSG);
    }

    public static JsonResult success(Object body) {
        return new JsonResult(ErrorCode.SUCCESS_CODE,null, body);
    }

    public static JsonResult error() {
        return new JsonResult(ErrorCode.ERROR_CODE, ErrorCode.ERROR_MSG);
    }

    public static JsonResult error(String errorMsg) {
        return new JsonResult(ErrorCode.ERROR_CODE, errorMsg);
    }
}
