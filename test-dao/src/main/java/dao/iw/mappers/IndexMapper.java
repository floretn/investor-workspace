package dao.iw.mappers;

import models.Index;
import org.apache.ibatis.annotations.*;

public interface IndexMapper {

    @Select("select indx_pk, indx_name from investor_workspace.t_indexes where indx_pk = #{id}")
    Index selectIndex(int id);

    @Insert("insert into investor_workspace.t_indexes (indx_name)\n" +
            "values (#{index.indxName});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "index.indxPK", before = false, resultType = Integer.class)
    void insertIndex(@Param("index") Index index);

    @Delete("delete from investor_workspace.t_indexes where indx_pk = #{id};\n" +
            "commit;")
    void deleteIndex(int id);

    @Update("update investor_workspace.t_indexes set indx_name = #{index.indxName} where indx_pk = #{id};\n" +
            "commit;")
    void updateIndex(@Param("id") int id, @Param("index") Index index);
}
