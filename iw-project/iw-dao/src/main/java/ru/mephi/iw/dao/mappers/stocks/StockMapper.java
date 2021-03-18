package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StockMapper {

    @Select("select stck_pk, stck_cmpn_fk, stck_name, stck_ticker from investor_workspace.t_stocks where stck_ticker = #{ticker}")
    Stock selectStockByTicker(@Param("ticker") String ticker);

    @Insert("insert into investor_workspace.t_stocks (stck_cmpn_fk, stck_name, stck_ticker)\n" +
            "values (#{stock.companyId}, #{stock.name}, #{stock.ticker});" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "stock.id", before = false, resultType = Integer.class)
    void insertStock(@Param("stock") Stock stock);
}
