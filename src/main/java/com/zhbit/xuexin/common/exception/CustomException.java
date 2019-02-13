package com.zhbit.xuexin.common.exception;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private Object data;

    public CustomException(String message, String code) {
        super(message);
        this.code = code;
    }

    public CustomException(String message, String code, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
