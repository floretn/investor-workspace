package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DoicMapper {

    @Insert("insert into investor_workspace.t_date_of_indexes_changes (doic_indx_fk, doic_date_chng)\n" +
            "values (#{doic.indexId}, #{doic.dateOfChange});")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "doic.id", before = false, resultType = Integer.class)
    void insertDoic(@Param("doic") DateOfIndexesChanges doic);
}
