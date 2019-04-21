package com.kuaidi.query.demo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.kuaidi.query.demo.domain.ResultVO;
import com.kuaidi.query.demo.exception.BusinessException;
import com.kuaidi.query.demo.request.QueryRequest;
import com.kuaidi.query.demo.utils.HttpUtilManager;
import com.kuaidi.query.demo.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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
    private static Set<Cookie> cookies =null;
    private static Integer failedTimes=0;
    private static WebClient webClient = null;

    @PostConstruct
    synchronized WebClient getWebClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        try {
            webClient.getPage("https://www.kuaidi100.com");
        } catch (IOException e) {
            log.error("getPage init error",e);
        }
        webClient.waitForBackgroundJavaScript(5000);
        cookies = webClient.getCookieManager().getCookies();
        return webClient;
    }

    @Async
    public synchronized void check(String response){
        if (response.contains(":\"查无结果\"")) {
            failedTimes++;
        } else {
            failedTimes = 0;
        }
        if (failedTimes > 5) {
            getWebClient();
        }
    }

    public void reset(){
        getWebClient();
    }

    public synchronized ResultVO query(QueryRequest request) throws IOException {
        log.info("query request: request={}",request);
        cookies.forEach(item->{
            webClient.getCookieManager().addCookie(item);
        });
        HtmlPage page = webClient.getPage(String.format("https://www.kuaidi100.com/query?type=%s&postid=%s&temp=%s&phone=%s",request.getType(),request.getPostId(),Math.random(),request.getPhone()==null?"":request.getPhone()));
        HtmlElement page1 = page.getBody();
        String response = page1.asText();
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
