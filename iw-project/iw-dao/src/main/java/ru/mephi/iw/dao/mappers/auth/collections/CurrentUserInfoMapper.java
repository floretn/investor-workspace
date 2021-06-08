package ru.mephi.iw.dao.mappers.auth.collections;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;

public interface CurrentUserInfoMapper {
    CurrentUserInfo selectCurrentUserInfo(@Param("login") String login, @Param("pwd") String pwd);
}
