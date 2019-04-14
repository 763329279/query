package com.kuaidi.query.demo.service;

import java.io.IOException;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.kuaidi.query.demo.domain.ResultVO;
import com.kuaidi.query.demo.exception.BusinessException;
import com.kuaidi.query.demo.request.QueryRequest;
import com.kuaidi.query.demo.utils.HttpUtilManager;
import com.kuaidi.query.demo.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author qxx on 2019/3/21.
 */
@Slf4j
@Service
public class QueryService {

    @Value("${customer}")
    private String customer;
    @Value("${key}")
    private String key;
    private static WebClient webClient = getWebClient();

    private static WebClient getWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        //webClient.setCssErrorHandler(new SilentCssErrorHandler());
        //webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        //webClient.getOptions().setTimeout(50000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        //webClient.waitForBackgroundJavaScript(10);
        try {
            webClient.getPage("https://www.kuaidi100.com/");
        } catch (IOException e) {
            log.error("getPage init error",e);
        }
        return webClient;
    }

    public synchronized ResultVO query(QueryRequest request) throws IOException {
        log.info("query request: request={}",request);
        HtmlPage page = webClient.getPage(String.format("https://www.kuaidi100.com/query?type=%s&postid=%s&temp=%f&phone=%s",request.getType(),request.getPostId(),Math.random(),request.getPhone()==null?"":request.getPhone()));
        HtmlElement page1 = page.getBody();
        String response = page1.asText();
        webClient.close();
        log.info("query response original: response={}",response);
        return JSON.parseObject(response, ResultVO.class);
    }

    /**
     * 通过正常接口调用
     */
    public ResultVO queryApi(QueryRequest request) {
        String param = request.toQueryAPIStr();
        log.info("queryApi params: param={}",param);
        String sign = MD5Util.md5Hex(param+key+customer);
        HashMap<String,String> params = new HashMap<>(3);
        params.put("param",param);
        params.put("sign",sign.toUpperCase());
        params.put("customer",customer);
        ResultVO resultVO;
        try {
            HttpUtilManager httpUtil = HttpUtilManager.getInstance();
            String resp = httpUtil.requestHttpPost("http://poll.kuaidi100.com/poll/query.do", "", params);
            log.info("query response original: response={}",resp);
            resultVO = JSON.parseObject(resp, ResultVO.class);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new BusinessException(e.getMessage());
        }
        return resultVO;
    }
}
