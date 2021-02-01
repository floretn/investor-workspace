package dao.iw.mappers;

import models.StocksPrices;
import org.apache.ibatis.annotations.*;

public interface SpMapper {

    @Select("select sp_pk, sp_stck_fk, sp_cur_fk, sp_time_set, sp_price from investor_workspace.t_stocks_prices where sp_pk = #{id}")
    StocksPrices selectSp(int id);

    @Insert("insert into investor_workspace.t_stocks_prices (sp_cur_fk, sp_stck_fk, sp_time_set, sp_price)\n" +
            "values (#{sp.spCurFK}, #{sp.spStckFK}, #{sp.spTimeSet}, #{sp.spPrice});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sp.spPK", before = false, resultType = Integer.class)
    void insertSp(@Param("sp") StocksPrices sp);

    @Delete("delete from investor_workspace.t_stocks_prices where sp_pk = #{id};\n" +
            "commit;")
    void deleteSp(int id);

    //TODO работает плохо, исправить.
    @Update("update investor_workspace.t_stocks_prices set sp_stck_fk = #{sp.spStckFK},\n" +
            "sp_cur_fk = #{sp.spCurFK},\n" +
            "sp_time_set = #{sp.spTimeSet},\n" +
            "sp_price = #{sp.spPrice} where sp_pk = #{id};\n" +
            "commit;")
    void updateSp(@Param("id") int id, @Param("sp") StocksPrices sp);
}
