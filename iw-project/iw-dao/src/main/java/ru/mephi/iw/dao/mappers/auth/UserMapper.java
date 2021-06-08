package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.User;

import java.util.List;

public interface UserMapper {

    User selectUser(@Param("id") int id);

    List<User> selectAllUsers();

    List<User> selectUserByUN(@Param("userName") String userName);

    void insertUser(@Param("user") User user);

    void updateUser(@Param("id") int id, @Param("user") User user);

    void deleteUser(@Param("id") int id);
}
