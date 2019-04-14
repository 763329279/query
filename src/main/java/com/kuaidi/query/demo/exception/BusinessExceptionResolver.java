package com.kuaidi.query.demo.exception;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import com.kuaidi.query.demo.domain.ErrorCode;
import com.kuaidi.query.demo.domain.JsonResult;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author xuejingtao
 * @date 2018/3/6
 */
@Component
public class BusinessExceptionResolver implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(BusinessExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        JsonResult jsonResult = ErrorCode.error("系统异常,请重试");
        ServletOutputStream outputStream = null;
        try {
            if (e instanceof VcbException) {
                VcbException be = (VcbException) e;
                jsonResult.setCode(be.getCode());
                jsonResult.setMsg(be.getMessage());
                logger.info(e.getMessage(), e);
            } else {
                logger.error(e.getMessage(), e);
            }
            if (!response.isCommitted()) {
                outputStream= response.getOutputStream();
                outputStream.write(JSONObject.toJSONString(jsonResult).getBytes(StandardCharsets.UTF_8));
            }
        }catch (Exception ex) {
            if (null != outputStream) {
                try {
                    outputStream.write(JSONObject.toJSONString(jsonResult).getBytes(StandardCharsets.UTF_8));
                } catch (IOException ignored) {
                }
            }else {
                logger.error("response.getWriter() is null");
            }
            logger.error(ex.getMessage(), ex);
        }
        return new ModelAndView();
    }
}
