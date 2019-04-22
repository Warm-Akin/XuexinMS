package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.ResumeTemplate;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeTemplateRepository extends IBaseRespository<ResumeTemplate, String> {
    List<ResumeTemplate> findByActive(Integer active);
}
