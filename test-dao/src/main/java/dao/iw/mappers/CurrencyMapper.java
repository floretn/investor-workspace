package dao.iw.mappers;

import models.Currency;
import org.apache.ibatis.annotations.*;

public interface CurrencyMapper {

    @Select("select cur_pk, cur_name from investor_workspace.t_currency where cur_pk = #{id}")
    Currency selectCurrency(int id);

    @Insert("insert into investor_workspace.t_currency (cur_name)\n" +
            "values (#{currency.curName});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "currency.curPK", before = false, resultType = Integer.class)
    void insertCurrency(@Param("currency") Currency currency);

    @Delete("delete from investor_workspace.t_currency where cur_pk = #{id};\n" +
            "commit;")
    void deleteCurrency(int id);

    @Update("update investor_workspace.t_currency set cur_name = #{currency.curName} where cur_pk = #{id};\n" +
            "commit;")
    void updateCurrency(@Param("id") int id, @Param("currency") Currency currency);
}
