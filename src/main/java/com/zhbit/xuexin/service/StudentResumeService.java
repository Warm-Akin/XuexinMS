package com.zhbit.xuexin.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.StudentResumeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;

@Service
public class StudentResumeService {

    @Autowired
    StudentResumeRepository resumeRepository;

    @Autowired
    StudentRepository studentRepository;

    public StudentResume findByStudentNo(String studentNo) {

        StudentResume studentResume = null;
        if (!StringUtils.isEmpty(studentNo)) {
            studentResume = resumeRepository.findByStudentNo(studentNo);
            if (null == studentResume) {
                studentResume = new StudentResume();
                Student student = studentRepository.findByStudentNo(studentNo);
                // copy the same properties from Student
                BeanUtils.copyProperties(student, studentResume);
            }
        }
        return studentResume;
    }

    @Transactional
    public void handleSave(StudentResume studentResume) {
        resumeRepository.save(studentResume);
    }

    @Transactional
    public void saveResumePhoto(MultipartFile file, String studentNo) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
            String targetFileName = studentNo + suffixName;
            String targetPath = Constant.PHOTO_DIRECTORY_PATH + targetFileName;
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(targetFile);
                studentResume.setPhotoPath(targetPath);
                resumeRepository.save(studentResume);
//                System.out.println("success");
            } catch (IOException e) {
                throw new CustomException(ResultEnum.SaveResumePhotoException.getMessage(), ResultEnum.SaveResumePhotoException.getCode());
            }
        } else
            throw new CustomException(ResultEnum.ResumePhotoIsNullException.getMessage(), ResultEnum.ResumePhotoIsNullException.getCode());
    }

    public void exportResume(String studentNo) {
        if (!StringUtils.isEmpty(studentNo)) {
            StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
            createPdf(studentResume);
        }
    }

    public void createPdf (StudentResume studentResume) {
        String templatePath = "C:\\Users\\Ahn\\Desktop\\xuexin_document\\resume-one.pdf";
        String targetPath = "C:\\Users\\Ahn\\Desktop\\xuexin_document\\resume-one-1.pdf";
        PdfReader pdfReader = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream bos;
        PdfStamper stamper = null;
        try {
            outputStream = new FileOutputStream(targetPath);
            pdfReader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(pdfReader, bos);
            AcroFields fields = stamper.getAcroFields();

            for (Field field : studentResume.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (!"photoPath".equals(field.getName())) {
                        fields.setField(field.getName(), field.get(studentResume).toString());
                    } else {
                        String imagePath = field.get(studentResume).toString();
                        // 通过域名获取所在页和坐标，左下角为起点
                        int pageNo = fields.getFieldPositions("photoPath").get(0).page;
                        Rectangle signRect = fields.getFieldPositions("photoPath").get(0).position;
                        fields.setField("photo", " ");
                        float x = signRect.getLeft();
                        float y = signRect.getBottom();
                        // 读图片
                        Image image = Image.getInstance(imagePath);
                        // 获取操作的页面
                        PdfContentByte under = stamper.getOverContent(pageNo);
                        // 根据域的大小缩放图片
                        image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                        // 添加图片
                        image.setAbsolutePosition(x, y);
                        under.addImage(image);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, outputStream);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
