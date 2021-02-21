package ru.mephi.iw.dao.mappers;

import ru.mephi.iw.models.StocksInIndexes;
import ru.mephi.iw.models.StocksPrices;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SpMapper {

    @Select("select sp_pk, sp_stck_fk, sp_cur_fk, sp_time_set, sp_price from investor_workspace.t_stocks_prices where sp_pk = #{id}")
    StocksPrices selectSp(int id);

    @Select("select sp_pk, sp_stck_fk, sp_cur_fk, sp_time_set, sp_price from investor_workspace.t_stocks_prices")
    List<StocksPrices> selectAllSp();

    @Select("select * from (\n" +
            "               select sp_pk, sp_stck_fk, sp_cur_fk, sp_time_set, sp_price from investor_workspace.t_stocks_prices\n" +
            "               order by sp_time_set desc\n" +
            "               limit 45\n" +
            "               )  li\n" +
            "order by sp_pk")
    List<StocksPrices> selectAllSpLastIMOEX();

    @Insert("insert into investor_workspace.t_stocks_prices (sp_stck_fk, sp_cur_fk, sp_time_set, sp_price)\n" +
            "values (#{sp.stockId}, #{sp.currencyId}, #{sp.settingTime}, #{sp.price});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sp.id", before = false, resultType = Integer.class)
    void insertSp(@Param("sp") StocksPrices sp);

    @Delete("delete from investor_workspace.t_stocks_prices where sp_pk = #{id};\n" +
            "commit;")
    void deleteSp(int id);

    @Update("update investor_workspace.t_stocks_prices set sp_stck_fk = #{sp.stockId},\n" +
            "sp_cur_fk = #{sp.currencyId},\n" +
            "sp_time_set = #{sp.settingTime},\n" +
            "sp_price = #{sp.price} where sp_pk = #{id};\n" +
            "commit;")
    void updateSp(@Param("id") int id, @Param("sp") StocksPrices sp);
}
