package com.kuaidi.query.demo.service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
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
    private static Set<Cookie> cookie=null;
    private static Integer failedTimes=0;
    private static  HashMap<String, String> map;

    static {
        map = new HashMap<>(8);
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        map.put("Accept-Encoding","gzip, deflate, br");
        map.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Cache-Control","max-age=0");
        map.put("Connection","keep-alive");
        map.put("Host","www.kuaidi100.com");
        map.put("Upgrade-Insecure-Requests","1");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
    }

    @PostConstruct
    WebClient getWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
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
        webClient.waitForBackgroundJavaScript(10000);
        cookie = webClient.getCookieManager().getCookies();
        webClient.close();
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

    public ResultVO query(QueryRequest queryRequest) throws IOException {
        log.info("query request: request={}",queryRequest);
        String url=String.format("https://www.kuaidi100.com/query?type=%s&postid=%s&temp=%f&phone=%s",queryRequest.getType(),queryRequest.getPostId(),Math.random(),queryRequest.getPhone()==null?"":queryRequest.getPhone());
        URL link=new URL(url);
        WebClient wc=new WebClient();
        WebRequest request=new WebRequest(link);
        request.setCharset(Charset.forName("UTF-8"));
        request.setAdditionalHeaders(map);

        //其他报文头字段可以根据需要添加
        wc.getCookieManager().setCookiesEnabled(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        for (Cookie cookie1 : cookie) {
            wc.getCookieManager().addCookie(cookie1);
        }
        HtmlPage  page = wc.getPage(request);
        HtmlElement page1 = page.getBody();
        String response = page1.asText();
        check(response);
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
