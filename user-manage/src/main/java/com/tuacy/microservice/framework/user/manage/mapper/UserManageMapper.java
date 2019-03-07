package com.tuacy.microservice.framework.user.manage.mapper;

import com.tuacy.microservice.framework.user.manage.api.response.UserInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserManageMapper {

    List<String> selectName();

    long saveUserInfo(@Param("userInfo") UserInfoEntity userInfo);

}
