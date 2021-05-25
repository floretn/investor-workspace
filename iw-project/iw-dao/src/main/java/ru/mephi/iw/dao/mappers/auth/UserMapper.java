package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.User;

public interface UserMapper {

    User selectUser(@Param("id") int id);

    void insertUser(@Param("user") User user);

    void updateUser(@Param("id") int id, @Param("user") User user);
}
