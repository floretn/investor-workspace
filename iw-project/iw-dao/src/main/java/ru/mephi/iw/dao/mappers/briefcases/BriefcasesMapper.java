package ru.mephi.iw.dao.mappers.briefcases;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.briefcases.Briefcases;
import java.util.List;

public interface BriefcasesMapper {

    Briefcases selectBriefcase(@Param("id") int id);

    List<Briefcases> selectBriefcasesOfUser(@Param("userId") int userId);

    void insertBriefcase(@Param("briefcase") Briefcases briefcase);

    void updateBriefcase(@Param("id") int id, @Param("briefcase") Briefcases briefcase);

    void deleteBriefcases(@Param("id") int id);
}
