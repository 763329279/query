package com.kuaidi.query.demo.domain;

/**
 * 快递单当前签收状态
 * @author qxx
 */
public enum TypeEnum {

    //对话中没有设置机器人 jd|shunfeng|wjkwl
    JD("jd", "京东"),
    //机器人会话中
    SHUN_FENG ("shunfeng", "顺丰"),
    //正在等待分配客服中
    WJKWL("wjkwl", "万家康");

    private String code;
    private String name;

    TypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    //public static String getStatusName(String value) {
    //    TypeEnum[] resultStatuses = values();
    //    for (TypeEnum resultStatus : resultStatuses) {
    //
    //    }
    //}
}
