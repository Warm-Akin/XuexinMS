package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.response.ResumeTemplateResultVO;
import com.zhbit.xuexin.model.ResumeTemplate;
import com.zhbit.xuexin.repository.ResumeTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeTemplateService {

    @Autowired
    ResumeTemplateRepository resumeTemplateRepository;

//    public List<ResumeTemplateResultVO> getAllActiveTemplateInfo() {
//        List<ResumeTemplateResultVO> resumeTemplateResultVOList = new ArrayList<>();
//        List<ResumeTemplate> resumeTemplateList = resumeTemplateRepository.findByActive(Constant.ACTIVE);
//        if (!resumeTemplateList.isEmpty()) {
//            ResumeTemplateResultVO resumeTemplateResultVO = new ResumeTemplateResultVO();
//            resumeTemplateList.forEach(resumeTemplate -> {
//                resumeTemplateResultVO.setTemplateName(resumeTemplate.getTemplateName());
//                // transfer image to byte[]
//                FileInputStream fileInputStream = null;
//                try {
//                    fileInputStream = new FileInputStream(new File(resumeTemplate.getImageUrl()));
//                    byte[] bytes = new byte[fileInputStream.available()];
//                    fileInputStream.read(bytes,0, fileInputStream.available());
//                    resumeTemplateResultVO.setImageBytes(bytes);
//                    resumeTemplateResultVOList.add(resumeTemplateResultVO);
//                    fileInputStream.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        return resumeTemplateResultVOList;
//    }

    public byte[] getAllActiveTemplateInfo() {
        List<ResumeTemplate> resumeTemplateList = resumeTemplateRepository.findByActive(Constant.ACTIVE);
        byte[] bytes = null;
        if (!resumeTemplateList.isEmpty()) {
            // transfer image to byte[]
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(resumeTemplateList.get(0).getImageUrl()));
                bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes, 0, fileInputStream.available());
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
