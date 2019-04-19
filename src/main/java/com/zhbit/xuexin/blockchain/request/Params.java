package com.zhbit.xuexin.blockchain.request;

import lombok.Data;

//@Data
public class Params {

    private int type;

    private ChainCode chainCode;

    private CtorMsg ctorMsg;

    private String secureContext;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ChainCode getChainCode() {
        return chainCode;
    }

    public void setChainCode(ChainCode chainCode) {
        this.chainCode = chainCode;
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
