package ru.mephi.iw.dao.mappers.briefcases;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.briefcases.BriefcaseStates;

import java.time.LocalDate;
import java.util.List;

public interface BSMapper {

    BriefcaseStates selectLastBSOfBriefcase(@Param("briefcaseId") int briefcaseId);

    BriefcaseStates selectBSOfBriefcaseByDate(@Param("briefcaseId") int briefcaseId, @Param("date") LocalDate date);

    List<BriefcaseStates> selectAllBSOfBriefcase(@Param("briefcaseId") int briefcaseId);

    void insertBS(@Param("bs") BriefcaseStates bs);

    void deleteBS(@Param("id") int id);
}
