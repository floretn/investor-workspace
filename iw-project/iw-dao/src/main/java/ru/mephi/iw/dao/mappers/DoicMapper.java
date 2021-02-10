package ru.mephi.iw.dao.mappers;

import ru.mephi.iw.models.DateOfIndexesChanges;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DoicMapper {

    @Select("select doic_pk, doic_indx_fk, doic_date_chng from investor_workspace.t_date_of_indexes_changes where doic_pk = #{id}")
    DateOfIndexesChanges selectDoic(int id);

    @Select("select doic_pk, doic_indx_fk, doic_date_chng from investor_workspace.t_date_of_indexes_changes")
    List<DateOfIndexesChanges> selectAllDoic();

    @Insert("insert into investor_workspace.t_date_of_indexes_changes (doic_indx_fk, doic_date_chng)\n" +
            "values (#{doic.indexId}, #{doic.dateOfChange});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "doic.id", before = false, resultType = Integer.class)
    void insertDoic(@Param("doic") DateOfIndexesChanges doic);

    @Delete("delete from investor_workspace.t_date_of_indexes_changes where doic_pk = #{id};\n" +
            "commit;")
    void deleteDoic(int id);

    @Update("update investor_workspace.t_date_of_indexes_changes set doic_indx_fk = #{doic.indexId},\n" +
            "doic_date_chng = #{doic.dateOfChange} where doic_pk = #{id};\n" +
            "commit;")
    void updateDoic(@Param("id") int id, @Param("doic") DateOfIndexesChanges doic);
}
