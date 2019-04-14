package com.kuaidi.query.demo.request;

import javax.validation.constraints.Pattern;

import com.kuaidi.query.demo.domain.Constants;
import com.kuaidi.query.demo.domain.VcbPayload.InvalidParameter;
import com.kuaidi.query.demo.domain.VcbPayload.MissingParameter;
import com.kuaidi.query.demo.utils.ValidatorHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author qxx on 2019/3/22.
 */
@Getter
@Setter
@ToString
public class QueryRequest {

    @NotBlank(payload = MissingParameter.class, message = "type")
    @Pattern(regexp="^(jd|shunfeng|wjkwl)$",payload = InvalidParameter.class, message = "format error")
    private String type;

    @NotBlank(payload = MissingParameter.class, message = "postId")
    private String postId;

    @Pattern(regexp= Constants.MOBILE_REGEX_STR,payload = InvalidParameter.class,message="format error")
    private String phone;

    /**
     * 转化成 API接口调用参数
     * @return
     */
    public String toQueryAPIStr() {
        return String.format("{\"com\":\"%s\",\"num\":\"%s\",\"phone\":\"%s\"}",type,postId,phone);
    }
    public static void main(String[] args) {
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.type = "wjkwl";
        queryRequest.postId = "shunfeng";
        queryRequest.phone = "";
        ValidatorHelper.validator(queryRequest);
    }
}
