package ru.mephi.iw.dao.mappers.briefcases.collections;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.briefcases.collections.BriefcaseInfoForUser;
import java.util.Date;
import java.util.List;

public interface BIFUMapper {

    BriefcaseInfoForUser selectLastBIFU(@Param("briefcaseId") int briefcaseId);

    BriefcaseInfoForUser selectBIFUByDate(@Param("briefcaseId") int briefcaseId, @Param("date") Date date);

    List<BriefcaseInfoForUser> selectAllLastBIFU(@Param("userId") int userId);
}
