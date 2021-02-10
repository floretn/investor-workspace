package ru.mephi.iw.dao.mappers;

import ru.mephi.iw.models.Index;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IndexMapper {

    @Select("select indx_pk, indx_name from investor_workspace.t_indexes where indx_pk = #{id}")
    Index selectIndex(int id);

    @Select("select indx_pk, indx_name from investor_workspace.t_indexes")
    List<Index> selectAllIndexes();

    @Insert("insert into investor_workspace.t_indexes (indx_name)\n" +
            "values (#{index.name});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "index.id", before = false, resultType = Integer.class)
    void insertIndex(@Param("index") Index index);

    @Delete("delete from investor_workspace.t_indexes where indx_pk = #{id};\n" +
            "commit;")
    void deleteIndex(int id);

    @Update("update investor_workspace.t_indexes set indx_name = #{index.name} where indx_pk = #{id};\n" +
            "commit;")
    void updateIndex(@Param("id") int id, @Param("index") Index index);
}
