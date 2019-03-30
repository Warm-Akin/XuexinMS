package com.zhbit.xuexin.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.dto.ResumeDto;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.StudentResumeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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


    public void createPdf (StudentResume studentResume) throws IOException {
        Resource resource = new ClassPathResource("static\\pdfTemplates\\resume-one.pdf");

//        String templatePath = "C:\\Users\\Ahn\\Desktop\\xuexin_document\\resume-one.pdf";
        // todo change the target path
        String targetPath = "C:\\Users\\Ahn\\Desktop\\xuexin_document\\resume-one-1.pdf";
        PdfReader pdfReader = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream bos;
        PdfStamper stamper = null;
        try {
            // todo add
            File resourceFile = resource.getFile();
            InputStream pdfInputStream = new FileInputStream(resourceFile);
            outputStream = new FileOutputStream(targetPath);
//            pdfReader = new PdfReader(templatePath);
            pdfReader = new PdfReader(pdfInputStream);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(pdfReader, bos);
            AcroFields fields = stamper.getAcroFields();

            // todo 模板没有对应的表单域对象需要跳过
            for (Field field : studentResume.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    // todo 做判空处理
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

    public byte[] getImageByStudentNo(String studentNo) throws IOException {
        StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
        if (null != studentResume && !StringUtils.isEmpty(studentResume.getPhotoPath())) {
            String photoPath = studentResume.getPhotoPath();
            FileInputStream fileInputStream = new FileInputStream(new File(photoPath));
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes,0, fileInputStream.available());
            return bytes;
        }
        return null;
    }

    public PageResultVO<StudentResume> findAll(Integer page, Integer pageSize) {
        // page's value start at '0'
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<StudentResume> studentResumePage = (Page<StudentResume>) resumeRepository.findAll(new Specification<StudentResume>() {

            @Override
            public Predicate toPredicate(Root<StudentResume> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageRequest);
        PageResultVO<StudentResume> pageResultVO = new PageResultVO<>(studentResumePage.getContent(), studentResumePage.getTotalElements());
        return pageResultVO;
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "studentName"));
        return new Sort(orders);
    }

    @Transactional
    public void deleteRecords(List<StudentResume> resumeList) {
        if (resumeList.size() > 0) {
            resumeList.forEach(resume -> {
                resume.setActive(Constant.INACTIVE);
            });
            resumeRepository.saveAll(resumeList);
        } else
            throw new CustomException(ResultEnum.ResumeDeleteFailedException.getMessage(), ResultEnum.ResumeDeleteFailedException.getCode());
    }

    public PageResultVO<StudentResume> findByConditions(ResumeDto resumeDto) {
        Pageable pageable = PageRequest.of(resumeDto.getCurrentPage() - 1, resumeDto.getPageSize(), getSort());
        Page<StudentResume> resultPage = resumeRepository.findAll(new Specification<StudentResume>() {

            @Override
            public Predicate toPredicate(Root<StudentResume> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(resumeDto.getStudentName())) {
                    Path studentName = root.get("studentName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(studentName), "%" + resumeDto.getStudentName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(resumeDto.getJobWant())) {
                    Path jobWant = root.get("jobWant");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(jobWant), "%" + resumeDto.getJobWant().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(resumeDto.getMajor())) {
                    Path major = root.get("major");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(major), "%" + resumeDto.getMajor().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(resumeDto.getSchoolName())) {
                    Path schoolName = root.get("schoolName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(schoolName), "%" + resumeDto.getSchoolName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                // active = 1
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageable);

        PageResultVO<StudentResume> pageResultVO = new PageResultVO<>(resultPage.getContent(), resultPage.getTotalElements());
        return pageResultVO;
    }
}
