package ru.mephi.iw.dao.mappers.auth;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.Pages;

public interface PagesMapper {

    Pages selectPages(@Param("id") int id);

}
