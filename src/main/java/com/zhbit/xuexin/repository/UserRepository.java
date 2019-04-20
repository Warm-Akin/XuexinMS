package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  UserRepository extends IBaseRespository<User, String> {

    User findByEmployNoAndPassword(String userName, String password);

    User findByEmployNo(String username);

    @Query(value = "select * FROM tb_sys_user where EMPLOY_NO IN (?1)", nativeQuery = true)
    List<User> findByEmployNoList(List<String> employNoList);
}
