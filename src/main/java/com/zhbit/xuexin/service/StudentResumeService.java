package com.zhbit.xuexin.service;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.zhbit.xuexin.blockchain.config.BlockChainUtil;
import com.zhbit.xuexin.blockchain.model.BCResult;
import com.zhbit.xuexin.blockchain.util.ObjectTransferUtil;
import com.zhbit.xuexin.common.config.QiniuConfig;
import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.QiniuUtil;
import com.zhbit.xuexin.dto.ResumeDto;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.model.ResumeTemplate;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.repository.CompanyRepository;
import com.zhbit.xuexin.repository.ResumeTemplateRepository;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.StudentResumeRepository;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class StudentResumeService {

    @Autowired
    StudentResumeRepository resumeRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ResumeTemplateRepository resumeTemplateRepository;

    @Autowired
    Executor executor;

    @Autowired
    QiniuUtil qiniuUtil;

    @Autowired
    QiniuConfig qiniuConfig;

//    @Autowired
//    BlockChainUtil blockChainUtil;
//
//    @Autowired
//    ObjectTransferUtil objectTransferUtil;

    //        String tableName = "Students";


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

//        // todo  query from BlockChain
//        String[] resumeStringArray = objectTransferUtil.convertToStringArray(studentResume);
//        String result = blockChainUtil.queryTable(tableName, resumeStringArray);
//        // transfer result to object
//        List<BCResult> nv = JSON.parseArray(result, BCResult.class);

        return studentResume;
    }

    @Transactional
    public void handleSave(StudentResume studentResume) {
        // copy the same properties from studentInfo
        Student student = studentRepository.findByStudentNo(studentResume.getStudentNo());
        studentResume.setResumeUrl("");
        studentResume.setResumeImageUrl("");
        // false -> 没有创建pdf
        studentResume.setCreateFlag("false");
        BeanUtils.copyProperties(student, studentResume);
        resumeRepository.save(studentResume);
        //        studentRepository.save(student);

//        // todo save into blockChain
//        String[] resumeStringArray = objectTransferUtil.convertToStringArray(studentResume);
//        blockChainUtil.callTableFunction(tableName, "save", resumeStringArray);

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

    public void exportResume(String studentNo, String resumeTemplateId, HttpServletResponse response) {
        if (!StringUtils.isEmpty(studentNo)) {
            StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
            String resumeUrl = studentResume.getResumeUrl();
            String pdfUrlSuffix = "";
            if (null != resumeUrl && !StringUtils.isEmpty(resumeUrl) && resumeUrl.indexOf(resumeTemplateId) != -1) {
                // 已经生成过pdf, 返回该pdf的url即可
                pdfUrlSuffix = resumeUrl;
            } else {
                // 生成
                pdfUrlSuffix = studentNo + "_" + resumeTemplateId;
                ResumeTemplate resumeTemplate = resumeTemplateRepository.findById(resumeTemplateId).orElse(null);
                String targetPath = Constant.STUDENT_RESUME_PDF_PATH + pdfUrlSuffix + ".pdf";
                createPdf(studentResume, targetPath, resumeTemplate.getTemplateUrl());
                // set pdf path
                studentResume.setResumeUrl(pdfUrlSuffix);
                // 异步保存数据和生成缩略图
                asyncHandlerStudentResume(studentResume, targetPath);
            }
            exportPdf(Constant.STUDENT_RESUME_PDF_PATH + pdfUrlSuffix + ".pdf", response);
        }
    }


    public void createPdf(StudentResume studentResume, String targetPath, String templatePath) {
        // read template file
        Resource resource = new FileSystemResource(templatePath);

        PdfReader pdfReader = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream bos;
        PdfStamper stamper = null;
        try {
            File resourceFile = resource.getFile();
            InputStream pdfInputStream = new FileInputStream(resourceFile);
            outputStream = new FileOutputStream(targetPath);
            pdfReader = new PdfReader(pdfInputStream);
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

    private void exportPdf(String targetPath, HttpServletResponse response) {
        File file = new File(targetPath);
        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
        response.setContentType("multipart/form-data");
        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        try {
            InputStream ins = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = ins.read(b)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
            os.close();
            ins.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public byte[] getImageByStudentNo(String studentNo) throws IOException {
        StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
        if (null != studentResume && !StringUtils.isEmpty(studentResume.getPhotoPath())) {
            String photoPath = studentResume.getPhotoPath();
            FileInputStream fileInputStream = new FileInputStream(new File(photoPath));
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes, 0, fileInputStream.available());
            fileInputStream.close();
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
                // create success
                Path createFlag = root.get("createFlag");
                Predicate p0 = criteriaBuilder.equal(createFlag, Constant.CREATE_FLAG_TRUE);
                predicateList.add(p0);

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
                // create success
                Path createFlag = root.get("createFlag");
                Predicate p0 = criteriaBuilder.equal(createFlag, Constant.CREATE_FLAG_TRUE);
                predicateList.add(p0);

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

    // 异步生成缩略图和保存数据
    private void asyncHandlerStudentResume(StudentResume studentResume, String targetPath) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // generate Image of student resume
                    String resumeImageUrl = generateImageAfterCreatePdf(targetPath, studentResume.getStudentNo());
                    // update student resume
                    updateAfterGenerateResumePdf(studentResume, resumeImageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Transactional
    protected void updateAfterGenerateResumePdf(StudentResume studentResume, String resumeImageUrl) {
        studentResume.setResumeImageUrl(resumeImageUrl);
        studentResume.setCreateFlag(Constant.CREATE_FLAG_TRUE);
        resumeRepository.save(studentResume);
    }

    private String generateImageAfterCreatePdf(String targetPath, String studentNo) {
        org.icepdf.core.pobjects.Document document = new org.icepdf.core.pobjects.Document();
        String imageUrl = Constant.STUDENT_RESUME_RESUME_IMAGE_PATH + "resume_" + studentNo + ".png";
        BufferedImage image = null;
        try {
            document.setFile(targetPath);
            // 缩放比例
            float scale = 1f;
            // 旋转角度
            float rotation = 0f;
            // 只生成pdf第一页 number 从0开始
            image = (BufferedImage) document.getPageImage(0, GraphicsRenderingHints.SCREEN, org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation, scale);
            RenderedImage rendImage = image;
            File targetFile = new File(imageUrl);
            ImageIO.write(rendImage, "png", targetFile);
        } catch (PDFException e) {
            e.printStackTrace();
        } catch (PDFSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            image.flush();
        }

        // 上传图片到七牛云
        String serverImageName = qiniuUtil.uploadImageToServer(imageUrl);
//        String serverImageUrl = qiniuConfig.getHttpPrefix() + qiniuConfig.getLinkName() + "/" + serverImageName;
        return qiniuConfig.getHttpPrefix() + qiniuConfig.getLinkName() + "/" + serverImageName;
    }

    public void exportStudentResume(String resumeUrl, HttpServletResponse response) {
        String filePath = Constant.STUDENT_RESUME_PDF_PATH + resumeUrl + ".pdf";
        exportPdf(filePath, response);
    }

    @Transactional
    public void exportStudentResumeForCompany(String soleCode, String resumeUrl, HttpServletResponse response) {
        Company company = companyRepository.findBySoleCode(soleCode);
        if (null != company) {
            String limitation = company.getPdfLimit();
            if (!limitation.equals("-1") && !limitation.equals("0")) {
                exportStudentResume(resumeUrl, response);
                String newLimitation = limitation.equals("+9999") ? limitation : String.valueOf(Integer.parseInt(limitation) - 1);
                company.setPdfLimit(newLimitation);
                companyRepository.save(company);
            } else
                throw new CustomException(ResultEnum.CompanyUploadResumeErrorException.getMessage(), ResultEnum.CompanyUploadResumeErrorException.getCode());
        } else
            throw new CustomException(ResultEnum.CompanySoleCodeErrorException.getMessage(), ResultEnum.CompanySoleCodeErrorException.getCode());
    }
}
