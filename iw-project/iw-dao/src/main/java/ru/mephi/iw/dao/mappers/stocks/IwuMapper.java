package ru.mephi.iw.dao.mappers.stocks;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.stocks.IndexWasUpload;
import java.time.LocalDate;

public interface IwuMapper {

    IndexWasUpload selectIwu(@Param("indexId") int indexId, @Param("date") LocalDate date);

    void insertIwu(@Param("iwu") IndexWasUpload iwu);
}
