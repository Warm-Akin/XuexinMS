package com.zhbit.xuexin.blockchain.request;

import org.springframework.stereotype.Component;

@Component
public class Params {

    private int type;

    private ChainCode chainCodeId;

    private CtorMsg ctorMsg;

    private String secureContext;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ChainCode getChainCodeId() {
        return chainCodeId;
    }

    public void setChainCodeId(ChainCode chainCodeId) {
        this.chainCodeId = chainCodeId;
    }

    public CtorMsg getCtorMsg() {
        return ctorMsg;
    }

    public void setCtorMsg(CtorMsg ctorMsg) {
        this.ctorMsg = ctorMsg;
    }

    public String getSecureContext() {
        return secureContext;
    }

    public void setSecureContext(String secureContext) {
        this.secureContext = secureContext;
    }
}
