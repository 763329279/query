package com.kuaidi.query.demo.common;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (MDC.get("TraceId") == null){
            MDC.put("TraceId", UUID.randomUUID().toString());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        if (handler instanceof HandlerMethod ) {
            if (response instanceof VcbHttpServletResponseWrapper) {
                VcbHttpServletResponseWrapper wrapper = (VcbHttpServletResponseWrapper) response;
                log.info("response={}", IOUtils.toString(wrapper.getBytes(), StandardCharsets.UTF_8.name()));
            }
        }
    }
}
