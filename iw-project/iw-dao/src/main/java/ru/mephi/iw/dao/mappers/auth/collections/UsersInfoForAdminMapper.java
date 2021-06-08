package ru.mephi.iw.dao.mappers.auth.collections;

import ru.mephi.iw.models.auth.collections.UsersInfoForAdmin;
import java.util.List;

public interface UsersInfoForAdminMapper {
    List<UsersInfoForAdmin> selectUserInfo();
}
