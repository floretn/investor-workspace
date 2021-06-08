package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.Roles;

import java.util.List;

public interface RolesMapper {

    Roles selectRoles(@Param("id") int id);

    List<Roles> selectAllRoles(@Param("idNot") int idNot);
}
