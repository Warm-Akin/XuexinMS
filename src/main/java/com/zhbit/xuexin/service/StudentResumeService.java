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

import javax.servlet.http.HttpServletResponse;
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
        // copy the same properties from resume
        Student student = studentRepository.findByStudentNo(studentResume.getStudentNo());
        BeanUtils.copyProperties(studentResume, student);
        resumeRepository.save(studentResume);
        studentRepository.save(student);
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
            } catch (IOException e) {
                throw new CustomException(ResultEnum.SaveResumePhotoException.getMessage(), ResultEnum.SaveResumePhotoException.getCode());
            }
        } else
            throw new CustomException(ResultEnum.ResumePhotoIsNullException.getMessage(), ResultEnum.ResumePhotoIsNullException.getCode());
    }

    public void exportResume(String studentNo, HttpServletResponse response) throws IOException {
        if (!StringUtils.isEmpty(studentNo)) {
            StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
            createPdf(studentResume);
            exportPdf("C:\\Users\\Ahn\\Desktop\\xuexin_document\\resume-one-1.pdf", response);
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

            // todo 模板没有对应的表单域对象需要跳过
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
                        fields.setField("photoPath", " ");
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

            stamper.setFormFlattening(true); // 如果为false那么生成的PDF文件还能编辑，一定要设为true
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

    private void exportPdf(String targetPath, HttpServletResponse response) throws IOException {
        File file = new File(targetPath);

        InputStream ins = new FileInputStream(file);
        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
        response.setContentType("multipart/form-data");
        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        try{
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = ins.read(b)) > 0){
                os.write(b,0,len);
            }
            os.flush();
            os.close();
            ins.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
