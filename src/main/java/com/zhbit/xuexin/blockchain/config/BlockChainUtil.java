package com.zhbit.xuexin.blockchain.config;

import com.alibaba.fastjson.JSON;
import com.zhbit.xuexin.blockchain.response.Fabric06Response;
import com.zhbit.xuexin.blockchain.util.FabricUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BlockChainUtil {

    // 使用UTF-8
    private String charset = "UTF-8";

    // 根据实际情况配置访问URL
    private String blockChainUrl = "http://120.78.135.76:7050/chaincode";

    // 根据实际情况配置chainCode的路径
    private String chainCodePath = "github.com/hyperledger/fabric/chaincode/education";

    // chainCode部署后反馈的链码
    private String chainCodeName = "007b21764b99af792cb03fb2069eeb88a2d073b7b72fca68b92fc0235609cb2231460a1f797566b7cc677bdadf8a07dc1bf225d40a7d7c20f5a35d009c9599dc";

    // HTTP Client对象

    CloseableHttpClient httpClient = null;

    // HTTP POST对象
    HttpPost httpPost = null;

    @Autowired
    FabricUtils fabricUtils;

    // method
    // 查询
    public String queryTable(String tableName, String[] keys) {
        try {
            //所有的访问都是HTTP POST
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(blockChainUrl);

            //调度查询功能
            String jsonStr = fabricUtils.buildQueryJson(chainCodeName, tableName, keys);

            System.out.println("将发送[" + jsonStr + "]");
            httpPost.setEntity(new StringEntity(jsonStr, charset));

            //发送HTTP POST请求并获得反馈
            CloseableHttpResponse response = httpClient.execute(httpPost);
            //部署命令的反馈处理
            if (response == null) {
                httpClient.close();
                throw new Exception("调度query失败");
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new Exception("调度query没有反馈，请检查区块链服务端");
            }
            jsonStr = EntityUtils.toString(entity, charset);
            // 将反馈转为可识别信息
            Fabric06Response fabric06Response = JSON.parseObject(jsonStr, Fabric06Response.class);
            if (fabric06Response != null && fabric06Response.getResult() != null && fabric06Response.getResult().getStatus() != null) {

                if (fabric06Response.getResult().getStatus().toUpperCase().equals("OK")) {
                    System.out.println("查询表[" + tableName + "] 反馈[" + fabric06Response.getResult().getMessage() + "]");
                    if (fabric06Response.getResult().getMessage().toUpperCase().equals("NULL")) {
                        throw new Exception("查询表[" + tableName + "]反馈空，应为KEY[" + keys[0] + "]不存在");
                    }
                    return fabric06Response.getResult().getMessage();
                }
            }
            System.out.println("查询表有误，请检查区块链服务端");
        } catch (Exception e) {
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // function 可以是 insert、update、delete
    private void callTableFunction(String tableName, String function, String[] args) throws Exception {
        try {
            // 所有的访问都是HTTP POST
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(blockChainUrl);

            //调度invoke功能，查询表内容
            String jsonStr = fabricUtils.buildInvokeJson(chainCodeName, tableName + "_" + function, args);
            System.out.println("将发送[" + jsonStr + "]");
            httpPost.setEntity(new StringEntity(jsonStr, charset));
            //发送HTTP POST请求并获得反馈
            CloseableHttpResponse response = httpClient.execute(httpPost);
            //部署命令的反馈处理
            if (response == null) {
                httpClient.close();
                throw new Exception("调度invoke失败");
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new Exception("调度invoke没有反馈，请检查区块链服务端");
            }
            jsonStr = EntityUtils.toString(entity, charset);
            //将反馈转为可识别信息
            Fabric06Response fabric06Response = JSON.parseObject(jsonStr, Fabric06Response.class);
            if (fabric06Response != null && fabric06Response.getResult() != null && fabric06Response.getResult().getStatus() != null) {
                if (fabric06Response.getResult().getStatus().toUpperCase().equals("OK")) {
                    System.out.println("对表[" + tableName + "]的[" + function + "]已发送，请通过查询确认结果。");
                    return;
                }
            }
            System.out.println("对表[" + tableName + "]的[" + function + "]失败，请检查区块链服务端");
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

}
