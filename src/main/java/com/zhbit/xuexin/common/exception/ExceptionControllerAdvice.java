package com.zhbit.xuexin.common.exception;

import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.ResponseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private final Logger logger = LogManager.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity exceptionHandler(Exception e, HttpServletRequest req) {
        ResponseEntity responseEntity;
        if (e instanceof CustomException) {
            logger.error("[Custom Exception]={}", e);
            responseEntity = ResponseUtil.responseCustomException(((CustomException) e).getCode(), e.getMessage(), ((CustomException) e).getData());
        } else {
            logger.error("[Global Exception]={}", e);
            e.printStackTrace();
            responseEntity = ResponseUtil.responseGlobalException(ResultEnum.UnknownException.getCode(), ResultEnum.UnknownException.getMessage());
        }
        return responseEntity;
    }
}
