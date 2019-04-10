package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends IBaseRespository<User, String> {

    User findByEmployNoAndPassword(String userName, String password);

    User findByEmployNo(String username);
}
