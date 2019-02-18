package com.zhbit.xuexin.common.response;

import java.util.List;

public class PageResultVO<T> {

    private List<T> pageResultList;
    private Long total;

    public PageResultVO(List<T> pageResultList, Long total) {
        this.pageResultList = pageResultList;
        this.total = total;
    }

    public List<T> getPageResultList() {
        return pageResultList;
    }

    public void setPageResultList(List<T> pageResultList) {
        this.pageResultList = pageResultList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
