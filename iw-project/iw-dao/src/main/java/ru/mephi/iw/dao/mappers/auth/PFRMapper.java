package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.PagesForRoles;
import java.util.List;

public interface PFRMapper {

    List<PagesForRoles> selectPFR(@Param("roleId") int roleId);

}
