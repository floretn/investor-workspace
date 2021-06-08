package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.AuthInfo;

import java.util.List;

public interface AuthInfoMapper {

    List<AuthInfo> selectAuthInfoOfUser(@Param("userId") int userId);

    AuthInfo selectAuthInfo(@Param("login") String login);

    void insertAuthInfo(@Param("authInfo")AuthInfo authInfo);

    void updateAuthInfo(@Param("id") int id, @Param("authInfo")AuthInfo authInfo);

    void deleteAuthInfo(@Param("id") int id);
}
