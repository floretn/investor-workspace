package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import org.apache.ibatis.annotations.*;

public interface DoicMapper {

    void insertDoic(@Param("doic") DateOfIndexesChanges doic);

    void updateDoic(@Param("id") int id, @Param("doic") DateOfIndexesChanges doic);
}
