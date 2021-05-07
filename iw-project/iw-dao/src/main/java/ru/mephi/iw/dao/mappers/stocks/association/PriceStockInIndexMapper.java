package ru.mephi.iw.dao.mappers.stocks.association;

import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import org.apache.ibatis.annotations.*;
import java.sql.Timestamp;
import java.util.List;

public interface PriceStockInIndexMapper {

    List<PriceStockInIndex> selectLastStocksPricesForIndex(@Param("indexId") int indexId);

    List<PriceStockInIndex> selectStocksPricesForIndexOnPeriod(@Param("indexId") int indexId, @Param("from") Timestamp from,
                                                               @Param("before") Timestamp before);

    List<PriceStockInIndex> selectLastStocksPricesForIndexBeforeDate(@Param("indexId") int indexId, @Param("before") Timestamp before);

    List<PriceStockInIndex> selectFirstStocksPricesForIndexAfterDate(@Param("indexId") int indexId, @Param("from") Timestamp from);
}
