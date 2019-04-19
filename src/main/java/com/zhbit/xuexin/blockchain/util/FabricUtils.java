package com.zhbit.xuexin.blockchain.util;

import com.alibaba.fastjson.JSON;
//import request.Chaincode;
//import request.CtorMsg;
//import request.Fabric06Req;
//import request.Params;

/**
 * Created by zhengjq on 2018/5/23.
 */
public class FabricUtils {
//
//    public static String buildDeployJson(String chaincodePath, String schoolName) {
//        Fabric06Req req = new Fabric06Req();
//        //固定版本号
//        req.setJsonrpc("2.0");
//        //固定的method
//        req.setMethod("deploy");
//
//        Params params = new Params();
//        //固定为1，表示chaincode是go语言开发的
//        params.setType(1);
//
//        CtorMsg msg = new CtorMsg();
//        //固定的function
//        msg.setFunction("init");
//        //学校名称
//        String[] args = new String[1];
//        args[0] = schoolName;
//        msg.setArgs(args);
//
//        Chaincode chaincode = new Chaincode();
//        //chaincode的名称，随意，这里固定
//        chaincode.setName("mycc");
//        //chaincode源码所在路径
//        chaincode.setPath(chaincodePath);
//
//        params.setChaincodeID(chaincode);
//        params.setCtorMsg(msg);
//
//        //目前默认使用了这个用户进行链接，熟悉后更换其他用户即可。
//        params.setSecureContext("jim");
//
//        req.setParams(params);
//        //固定id
//        req.setId(1);
//
//        return JSON.toJSONString(req);
//    }
//
//    public static String buildInvokeJson(String chaincodeName, String function, String[] args) {
//        Fabric06Req req = new Fabric06Req();
//        req.setJsonrpc("2.0");
//        req.setMethod("invoke");
//
//        Params params = new Params();
//        params.setType(1);
//
//        CtorMsg msg = new CtorMsg();
//        msg.setFunction(function);
//        msg.setArgs(args);
//
//        Chaincode chaincode = new Chaincode();
//        chaincode.setName(chaincodeName);
//
//        params.setChaincodeID(chaincode);
//        params.setCtorMsg(msg);
//
//        params.setSecureContext("jim");
//
//        req.setParams(params);
//
//        req.setId(2);
//
//        return JSON.toJSONString(req);
//    }
//
//    public static String buildQueryJson(String chaincodeName, String function, String[] args) {
//        Fabric06Req req = new Fabric06Req();
//        req.setJsonrpc("2.0");
//        req.setMethod("query");
//
//        Params params = new Params();
//        params.setType(1);
//
//        CtorMsg msg = new CtorMsg();
//        msg.setFunction(function);
//        msg.setArgs(args);
//
//        Chaincode chaincode = new Chaincode();
//        chaincode.setName(chaincodeName);
//
//        params.setChaincodeID(chaincode);
//        params.setCtorMsg(msg);
//
//        params.setSecureContext("jim");
//
//        req.setParams(params);
//
//        req.setId(3);
//
//        return JSON.toJSONString(req);
//    }
}
