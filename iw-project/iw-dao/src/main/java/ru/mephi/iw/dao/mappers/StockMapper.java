package ru.mephi.iw.dao.mappers;

import ru.mephi.iw.models.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StockMapper {

    @Select("select stck_pk, stck_cmpn_fk, stck_name, stck_ticker, stck_num from investor_workspace.t_stocks where stck_pk = #{id}")
    Stock selectStock(int id);

    @Select("select stck_pk, stck_cmpn_fk, stck_name, stck_ticker, stck_num from investor_workspace.t_stocks")
    List<Stock> selectAllStocks();

    @Insert("insert into investor_workspace.t_stocks (stck_cmpn_fk, stck_name, stck_ticker)\n" +
            "values (#{stock.companyId}, #{stock.name}, #{stock.ticker});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "stock.id", before = false, resultType = Integer.class)
    void insertStock(@Param("stock") Stock stock);

    @Delete("delete from investor_workspace.t_stocks where stck_pk = #{id};\n" +
            "commit;")
    void deleteStock(int id);

    @Update("update investor_workspace.t_stocks set stck_cmpn_fk = #{stock.companyId},\n" +
            "stck_name = #{stock.name},\n" +
            "stck_ticker = #{stock.ticker} where stck_pk = #{id};\n" +
            "commit;")
    void updateStock(@Param("id") int id, @Param("stock") Stock stock);
}
