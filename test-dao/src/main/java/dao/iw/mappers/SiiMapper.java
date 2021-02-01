package dao.iw.mappers;

import models.StocksInIndexes;
import org.apache.ibatis.annotations.*;

public interface SiiMapper {

    @Select("select sii_pk, sii_stck_fk, sii_doic_fk, sii_num_stck from investor_workspace.t_stocks_in_indexes where sii_pk = #{id}")
    StocksInIndexes selectSii(int id);

    @Insert("insert into investor_workspace.t_stocks_in_indexes (sii_stck_fk, sii_doic_fk, sii_num_stck)\n" +
            "values (#{sii.siiStckFK}, #{sii.siiDoicFK}, #{sii.siiNumStck});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sii.siiPK", before = false, resultType = Integer.class)
    void insertSii(@Param("sii") StocksInIndexes sii);

    @Delete("delete from investor_workspace.t_stocks_in_indexes where sii_pk = #{id};\n" +
            "commit;")
    void deleteSii(int id);

    @Update("update investor_workspace.t_stocks_in_indexes set sii_stck_fk = #{sii.siiStckFK},\n" +
            "sii_doic_fk = #{sii.siiDoicFK},\n" +
            "sii_num_stck = #{sii.siiNumStck} where sii_pk = #{id};\n" +
            "commit;")
    void updateSii(@Param("id") int id, @Param("sii") StocksInIndexes sii);
}
