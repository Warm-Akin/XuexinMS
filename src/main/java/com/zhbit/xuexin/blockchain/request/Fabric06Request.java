package com.zhbit.xuexin.blockchain.request;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Fabric06Request implements Serializable {

    private String jsonResponseCode;

    private String method;

    private Params params;

    private int id;

    public String getJsonResponseCode() {
        return jsonResponseCode;
    }

    public void setJsonResponseCode(String jsonResponseCode) {
        this.jsonResponseCode = jsonResponseCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
