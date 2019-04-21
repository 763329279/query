package com.kuaidi.query.demo.controller;

import java.io.IOException;

import com.kuaidi.query.demo.domain.ErrorCode;
import com.kuaidi.query.demo.domain.JsonResult;
import com.kuaidi.query.demo.domain.ResultStatus;
import com.kuaidi.query.demo.domain.ResultVO;
import com.kuaidi.query.demo.domain.TypeEnum;
import com.kuaidi.query.demo.exception.MissingParameter;
import com.kuaidi.query.demo.request.QueryRequest;
import com.kuaidi.query.demo.service.QueryService;
import com.kuaidi.query.demo.utils.ValidatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author qxx on 2019/3/21.
 */
@Controller
@Slf4j
public class QueryController {

    @Autowired
    private QueryService queryService;

    /**
     * rest 风格请求
     */
    @RequestMapping("/v1/api/query")
    @ResponseBody
    public JsonResult query(QueryRequest request)throws IOException {
        ValidatorHelper.validator(request);
        if (TypeEnum.SHUN_FENG.getCode().equals(request.getType())) {
            if (StringUtils.isBlank(request.getPhone())) {
                throw new MissingParameter("phone");
            } else {
                request.setPhone(request.getPhone().substring(7));
            }
        }
        ResultVO resultVO = queryService.query(request);
        return ErrorCode.success(ResultVO.addState(resultVO));
    }

    @RequestMapping("/v1/api/query/reset")
    @ResponseBody
    public JsonResult reset(QueryRequest request) {
        queryService.reset();
        return ErrorCode.success();
    }

    /**
     * restAPI 请求
     */
    @RequestMapping("/v1/api/kuaidi/query")
    @ResponseBody
    public JsonResult queryApi(QueryRequest request){
        ValidatorHelper.validator(request);
        if (TypeEnum.SHUN_FENG.getCode().equals(request.getType())) {
            if (StringUtils.isBlank(request.getPhone())) {
                throw new MissingParameter("phone");
            }
        }
        ResultVO resultVO = queryService.queryApi(request);
        return ErrorCode.success(ResultVO.addState(resultVO));
    }

    /**
     * 前端页面测试请求
     */
    @GetMapping("/v1/indexQuery")
    public ModelAndView indexQuery(QueryRequest request) {
        ValidatorHelper.validator(request);
        if (TypeEnum.SHUN_FENG.getCode().equals(request.getType())) {
            if (StringUtils.isBlank(request.getPhone())) {
                throw new MissingParameter("phone");
            } else {
                request.setPhone(request.getPhone().substring(7));
            }
        }
        ResultVO query;
        try {
            query = queryService.query(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("index", "error", e.getMessage());
        }

        if (query.getStatus() != 200) {
            return new ModelAndView("index", "error", query.getMessage());
        }
        query.setStateName(ResultStatus.getStatusName(query.getState()));
        return new ModelAndView("index", "query", query);
    }

    /**
     * 前端页面测试请求
     */
    @GetMapping("/v1/index.html")
    public String indexCard() {
        return "index";
    }
}
