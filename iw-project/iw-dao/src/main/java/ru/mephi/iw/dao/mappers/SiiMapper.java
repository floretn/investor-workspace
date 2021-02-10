package ru.mephi.iw.dao.mappers;

import ru.mephi.iw.models.StocksInIndexes;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SiiMapper {

    @Select("select sii_pk, sii_stck_fk, sii_doic_fk, sii_num_stck from investor_workspace.t_stocks_in_indexes where sii_pk = #{id}")
    StocksInIndexes selectSii(int id);

    @Select("select sii_pk, sii_stck_fk, sii_doic_fk, sii_num_stck from investor_workspace.t_stocks_in_indexes")
    List<StocksInIndexes> selectAllSii();

    @Insert("insert into investor_workspace.t_stocks_in_indexes (sii_stck_fk, sii_doic_fk, sii_num_stck)\n" +
            "values (#{sii.stockId}, #{sii.dateOfIndexesChangesId}, #{sii.numberOfStocksInIndex});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sii.id", before = false, resultType = Integer.class)
    void insertSii(@Param("sii") StocksInIndexes sii);

    @Delete("delete from investor_workspace.t_stocks_in_indexes where sii_pk = #{id};\n" +
            "commit;")
    void deleteSii(int id);

    @Update("update investor_workspace.t_stocks_in_indexes set sii_stck_fk = #{sii.stockId},\n" +
            "sii_doic_fk = #{sii.dateOfIndexesChangesId},\n" +
            "sii_num_stck = #{sii.numberOfStocksInIndex} where sii_pk = #{id};\n" +
            "commit;")
    void updateSii(@Param("id") int id, @Param("sii") StocksInIndexes sii);
}
