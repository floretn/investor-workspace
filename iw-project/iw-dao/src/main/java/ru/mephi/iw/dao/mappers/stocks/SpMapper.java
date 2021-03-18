package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.StocksPrices;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SpMapper {

    @Insert("insert into investor_workspace.t_stocks_prices (sp_stck_fk, sp_cur_fk, sp_time_set, sp_price)\n" +
            "values (#{sp.stockId}, #{sp.currencyId}, #{sp.settingTime}, #{sp.price});" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sp.id", before = false, resultType = Integer.class)
    void insertSp(@Param("sp") StocksPrices sp);
}
