package com.zhbit.xuexin.blockchain.request;

import org.springframework.stereotype.Component;

@Component
public class ChainCode {

    private String name;

    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
