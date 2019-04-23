package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.ResumeTemplate;
import com.zhbit.xuexin.repository.ResumeTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResumeTemplateService {

    @Autowired
    ResumeTemplateRepository resumeTemplateRepository;

    public List<ResumeTemplate> getAllActiveTemplateInfo() {
        List<ResumeTemplate> resumeTemplateList = resumeTemplateRepository.findByActiveOrderByCreateDateDesc(Constant.ACTIVE);
        return resumeTemplateList;
    }

    @Transactional
    public void removeTemplateList(List<String> pdfTemplateIdList) {
        if (!pdfTemplateIdList.isEmpty()) {
            List<ResumeTemplate> resumeTemplateList = resumeTemplateRepository.findByIdList(pdfTemplateIdList);
            if (!resumeTemplateList.isEmpty()) {
                resumeTemplateList.forEach(resumeTemplate -> resumeTemplate.setActive(Constant.INACTIVE));
                resumeTemplateRepository.saveAll(resumeTemplateList);
            } else
                throw new CustomException(ResultEnum.ResumeTemplateIdsErrorException.getMessage(), ResultEnum.ResumeTemplateIdsErrorException.getCode());
        } else
            throw new CustomException(ResultEnum.ParamsIsNullException.getMessage(), ResultEnum.ParamsIsNullException.getCode());
    }

}
