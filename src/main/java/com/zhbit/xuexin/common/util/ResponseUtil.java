package com.zhbit.xuexin.common.util;

import com.zhbit.xuexin.common.constant.HttpCode;
import com.zhbit.xuexin.common.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity success(Object object) {
        Result<Object> result = new Result<>(object);
        result.setCode(HttpCode.SUCCESS);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    public static ResponseEntity responseCustomException(String code, String message, Object data) {
        Result<Object> result = new Result<>(code);
        result.setMsg(message);
        result.setData(data);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    public static ResponseEntity responseGlobalException(String code, String message) {
        Result<Object> result = new Result<>(code);
        result.setMsg(message);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}
