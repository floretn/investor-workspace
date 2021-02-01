package dao.iw.mappers;

import models.Stock;
import org.apache.ibatis.annotations.*;

public interface StockMapper {

    @Select("select stck_pk, stck_cmpn_fk, stck_name, stck_ticker, stck_num from investor_workspace.t_stocks where stck_pk = #{id}")
    Stock selectStock(int id);

    @Insert("insert into investor_workspace.t_stocks (stck_cmpn_fk, stck_name, stck_ticker, stck_num)\n" +
            "values (#{stock.stckCmpnFK}, #{stock.stckName}, #{stock.stckTicker}, #{stock.stckNum});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "stock.stckPK", before = false, resultType = Integer.class)
    void insertStock(@Param("stock") Stock stock);

    @Delete("delete from investor_workspace.t_stocks where stck_pk = #{id};\n" +
            "commit;")
    void deleteStock(int id);

    @Update("update investor_workspace.t_stocks set stck_cmpn_fk = #{stock.stckCmpnFK},\n" +
            "stck_name = #{stock.stckName},\n" +
            "stck_ticker = #{stock.stckTicker},\n" +
            "stck_num = #{stock.stckNum} where stck_pk = #{id};\n" +
            "commit;")
    void updateStock(@Param("id") int id, @Param("stock") Stock stock);
}
