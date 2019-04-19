package com.zhbit.xuexin.blockchain.config;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

public class BlockChainUtil {

    // 使用UTF-8
    private String charset = "UTF-8";

    // 根据实际情况配置访问URL
    private String blockChainUrl = "http://120.78.135.76:7050/chaincode";

    // 根据实际情况配置chainCode的路径
    private String chainCodePath = "github.com/hyperledger/fabric/chaincode/education";

    // chaincode部署后反馈的链码
    private String chainCodeName = "007b21764b99af792cb03fb2069eeb88a2d073b7b72fca68b92fc0235609cb2231460a1f797566b7cc677bdadf8a07dc1bf225d40a7d7c20f5a35d009c9599dc";

    // HTTP Client对象
    CloseableHttpClient httpClient = null;

    // HTTP POST对象
    HttpPost httpPost = null;


}
