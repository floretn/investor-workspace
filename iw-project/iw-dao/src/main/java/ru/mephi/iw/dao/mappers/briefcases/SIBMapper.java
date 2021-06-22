package ru.mephi.iw.dao.mappers.briefcases;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.briefcases.StocksInBriefcases;
import java.util.List;

public interface SIBMapper {

    List<StocksInBriefcases> selectAllSIBInBS(@Param("bsId") int briefcaseStatesId);

    void insertSIB(@Param("sib") StocksInBriefcases sib);

    void deleteAllSIBInBS(@Param("bsId") int bsId);
}
