package com.zhbit.xuexin.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.Result;
import com.zhbit.xuexin.common.response.ResultEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void writeSuccessResponse(HttpServletResponse httpServletResponse, Result<T> response) throws IOException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(httpServletResponse.getWriter(), response);
    }

    public static void writeErrorResponse(HttpServletResponse httpServletResponse, Result<T> response, HttpStatus httpStatus) throws IOException {
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.setContentType("application/json; charset=utf-8");
//        httpServletResponse.getOutputStream().println(objectMapper.writeValueAsString(response));
//        throw new CustomException(ResultEnum.AccountInvalidException.getMessage(), ResultEnum.AccountInvalidException.getCode());
        objectMapper.writeValue(httpServletResponse.getWriter(), response);
    }
}
