package dao.iw.mappers;

import models.DateOfIndexesChanges;
import org.apache.ibatis.annotations.*;

public interface DoicMapper {

    @Select("select doic_pk, doic_indx_fk, doic_date_chng from investor_workspace.t_date_of_indexes_changes where doic_pk = #{id}")
    DateOfIndexesChanges selectDoic(int id);

    @Insert("insert into investor_workspace.t_date_of_indexes_changes (doic_indx_fk, doic_date_chng)\n" +
            "values (#{doic.doicIndxFK}, #{doic.doicDateChng});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "doic.doicPK", before = false, resultType = Integer.class)
    void insertDoic(@Param("doic") DateOfIndexesChanges doic);

    @Delete("delete from investor_workspace.t_date_of_indexes_changes where doic_pk = #{id};\n" +
            "commit;")
    void deleteDoic(int id);

    @Update("update investor_workspace.t_date_of_indexes_changes set doic_indx_fk = #{doic.doicIndxFK},\n" +
            "doic_date_chng = #{doic.doicDateChng} where doic_pk = #{id};\n" +
            "commit;")
    void updateDoic(@Param("id") int id, @Param("doic") DateOfIndexesChanges doic);
}
