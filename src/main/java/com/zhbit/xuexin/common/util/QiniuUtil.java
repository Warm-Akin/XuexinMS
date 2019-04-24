package com.zhbit.xuexin.common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zhbit.xuexin.common.config.QiniuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QiniuUtil {

    @Autowired
    QiniuConfig qiniuConfig;

    public void uploadImageToServer(String localImageUrl) {
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        // zone2 -> 华南地区
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        String serverImageName = localImageUrl.substring(localImageUrl.lastIndexOf("\\") + 1);

        Response response = null;
        try {
            response = uploadManager.put(localImageUrl, serverImageName, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
