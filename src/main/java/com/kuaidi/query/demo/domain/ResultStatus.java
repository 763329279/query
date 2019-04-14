package com.kuaidi.query.demo.domain;

/**
 * 快递单当前签收状态
 * @author qxx
 */
public enum ResultStatus {

    //对话中没有设置机器人
    WAY(0, "在途中"),
    //机器人会话中
    TAKEN (1, "已揽收"),
    //正在等待分配客服中
    DIFFICULT(2, "疑难"),
    //访客掉线
    SIGNED(3, "已签收"),
    //客服掉线
    SIGN_BACK(4, "退签"),
    //客服服务中
    DELIVERING(5, "同城派送中"),
    //访客正常关闭
    SEND_BACK(6, "退回"),
    //客服正常关闭
    TRANSFER (7, "转单");


    private int code;
    private String name;

    ResultStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getStatusName(int value) {
        ResultStatus[] resultStatuses = values();
        if (value < 0 || value > resultStatuses.length - 1) {
            return null;
        } else {
            return resultStatuses[value].name;
        }
    }
}
