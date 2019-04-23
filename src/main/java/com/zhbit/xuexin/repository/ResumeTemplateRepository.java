package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.ResumeTemplate;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeTemplateRepository extends IBaseRespository<ResumeTemplate, String> {

    List<ResumeTemplate> findByActiveOrderByCreateDateDesc(Integer active);

    @Query(value = "select * FROM t_pdftemplate where TEM_ID IN (?1)", nativeQuery = true)
    List<ResumeTemplate> findByIdList(List<String> pdfTemplateIdList);
}
