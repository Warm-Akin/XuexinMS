package com.zhbit.xuexin.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_pdftemplate")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResumeTemplate implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "TEM_ID", length = 32, updatable = false)
    private String templateId;

    @Column(name = "TEMPLATE_URL")
    private String templateUrl;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "ACTIVE")
    private Integer active;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
