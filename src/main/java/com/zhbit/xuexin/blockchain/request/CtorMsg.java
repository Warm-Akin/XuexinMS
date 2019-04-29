package com.zhbit.xuexin.blockchain.request;

import org.springframework.stereotype.Component;

@Component
public class CtorMsg {

    private String function;

    private String[] args;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
