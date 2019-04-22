package com.zhbit.xuexin.service;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zhbit.xuexin.common.config.QiniuConfig;
import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.model.ResumeTemplate;
import com.zhbit.xuexin.repository.ResumeTemplateRepository;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.UUID;

@Service
public class PdfModelService {

    @Autowired
    ResumeTemplateRepository resumeTemplateRepository;

    @Autowired
    QiniuConfig qiniuConfig;

    public void handlePdfFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Document document = new Document();
        File tempFile = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            tempFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
            // MultipartFile 转成 File
            file.transferTo(tempFile);
            String tempFilePath = tempFile.getPath();
            document.setFile(tempFilePath);

            // 写出目标文件 -> pdf File
            String templateUrl = writePdfTemplate(tempFile, fileName);

            String templateName = fileName.substring(0, fileName.lastIndexOf("."));

            // 生成缩略图
            String imageUrl = generateImageFromDocument(document, templateName);

            // 保存该模板的信息
            ResumeTemplate resumeTemplate = new ResumeTemplate();
            resumeTemplate.setTemplateName(templateName);
            resumeTemplate.setTemplateUrl(templateUrl);
            resumeTemplate.setImageUrl(imageUrl);
            resumeTemplate.setActive(Constant.ACTIVE);
            resumeTemplateRepository.save(resumeTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PDFException e) {
            e.printStackTrace();
        } catch (PDFSecurityException e) {
            e.printStackTrace();
        } finally {
            document.dispose();
            deleteFile(tempFile);
        }
    }

    private String writePdfTemplate(File tempFile, String fileName) {
        String pdfTargetDirectory = Constant.PDF_TEMPLATE_DIRECTORY_PATH;
        String pdfUrl = pdfTargetDirectory + "template_" + fileName;
        FileOutputStream outputStream = null;
        InputStream pdfInputStream = null;
        try {
            outputStream = new FileOutputStream(pdfUrl);
            pdfInputStream = new FileInputStream(tempFile);
            byte bytes[] = new byte[1024];
            int temp = 0;  //边读边写
            while ((temp = pdfInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pdfInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pdfUrl;
    }

    private String generateImageFromDocument(Document document, String fileName) {
        String pdfImageDirectory = Constant.PDF_IMAGE_DIRECTORY_PATH;
        String imageUrl = pdfImageDirectory + "pdfModel_" + fileName + ".png";
        BufferedImage image = null;
        // 缩放比例
        float scale = 1f;
        // 旋转角度
        float rotation = 0f;
        // 只生成pdf第一页 number 从0开始
        try {
            image = (BufferedImage) document.getPageImage(0, GraphicsRenderingHints.SCREEN, org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation, scale);
            RenderedImage rendImage = image;
            File targetFile = new File(imageUrl);
            ImageIO.write(rendImage, "png", targetFile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.flush();
        }
        // 上传图片
        uploadImageToServer(imageUrl);
        return imageUrl;
    }

    // delete file
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void uploadImageToServer(String localImageUrl) {
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        // zone2 -> 华南地区
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        String serverImageName = localImageUrl.substring(localImageUrl.lastIndexOf("\\") + 1);

        Response response = null;
        try {
            response = uploadManager.put(localImageUrl, serverImageName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }
}
