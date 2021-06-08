package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.RolesOfUsers;
import java.util.List;

public interface ROUMapper {

    List<RolesOfUsers> selectROU(@Param("userId") int userId);

    void insertROU(@Param("rou") RolesOfUsers rou);

    void updateROUStatus(@Param("id") int id, @Param("rou") RolesOfUsers rou);

    void deleteROU(@Param("id") int id);
}
