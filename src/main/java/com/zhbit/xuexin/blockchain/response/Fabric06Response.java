package com.zhbit.xuexin.blockchain.response;

import java.io.Serializable;

public class Fabric06Response implements Serializable {

    private String jsonResponseCode; // 版本号

    private BCResponseResult result;

    private int id;

    public String getJsonResponseCode() {
        return jsonResponseCode;
    }

    public void setJsonResponseCode(String jsonResponseCode) {
        this.jsonResponseCode = jsonResponseCode;
    }

    public BCResponseResult getResult() {
        return result;
    }

    public void setResult(BCResponseResult result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
