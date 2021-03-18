package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.StocksInIndexes;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SiiMapper {

    @Insert("insert into investor_workspace.t_stocks_in_indexes (sii_stck_fk, sii_doic_fk, sii_num_stck)\n" +
            "values (#{sii.stockId}, #{sii.dateOfIndexesChangesId}, #{sii.numberOfStocksInIndex});" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sii.id", before = false, resultType = Integer.class)
    void insertSii(@Param("sii") StocksInIndexes sii);
}
