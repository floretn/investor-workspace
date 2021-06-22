package ru.mephi.iw.dao.mappers.auth.collections;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.collections.UsersInfoForAdmin;
import java.util.List;

public interface UsersInfoForAdminMapper {
    List<UsersInfoForAdmin> selectAllUsersInfo();

    UsersInfoForAdmin selectUserInfo(@Param("userId") int userId);
}
