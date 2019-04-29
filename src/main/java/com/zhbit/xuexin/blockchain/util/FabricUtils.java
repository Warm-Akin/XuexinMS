package com.zhbit.xuexin.blockchain.util;

import com.alibaba.fastjson.JSON;
import com.zhbit.xuexin.blockchain.request.ChainCode;
import com.zhbit.xuexin.blockchain.request.CtorMsg;
import com.zhbit.xuexin.blockchain.request.Fabric06Request;
import com.zhbit.xuexin.blockchain.request.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FabricUtils {

    @Autowired
    Fabric06Request request;

    @Autowired
    Params params;

    @Autowired
    CtorMsg msg;

    @Autowired
    ChainCode chainCode;

    public String buildInvokeJson(String chainCodeName, String function, String[] args) {

        request.setJsonResponseCode("2.0");
        request.setMethod("invoke");

        msg.setFunction(function);
        msg.setArgs(args);

        chainCode.setName(chainCodeName);

        params.setType(1);
        params.setChainCodeId(chainCode);
        params.setCtorMsg(msg);
        params.setSecureContext("jim");

        request.setParams(params);

        request.setId(2);

        return JSON.toJSONString(request);
    }

    public String buildQueryJson(String chainCodeName, String function, String[] args) {
        request.setJsonResponseCode("2.0");
        request.setMethod("query");

        msg.setFunction(function);
        msg.setArgs(args);

        chainCode.setName(chainCodeName);

        params.setType(1);
        params.setChainCodeId(chainCode);
        params.setCtorMsg(msg);
        params.setSecureContext("jim");

        request.setParams(params);
        request.setId(3);

        return JSON.toJSONString(request);
    }
}
