package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.AuthInfo;

public interface AuthInfoMapper {

    AuthInfo selectAuthInfo(@Param("login") String login);

    void insertAuthInfo(@Param("authInfo")AuthInfo authInfo);

    void updateAuthInfo(@Param("id") int id, @Param("authInfo")AuthInfo authInfo);
}
