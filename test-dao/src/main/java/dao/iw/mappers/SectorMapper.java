package dao.iw.mappers;

import models.Sector;
import org.apache.ibatis.annotations.*;

public interface SectorMapper {

    @Select("select sctr_pk, sctr_name from investor_workspace.t_sector where sctr_pk = #{id}")
    Sector selectSector(int id);

    @Insert("insert into investor_workspace.t_sector (sctr_name)\n" +
            "values (#{sector.sctrName});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "sector.sctrPK", before = false, resultType = Integer.class)
    void insertSector(@Param("sector") Sector sector);

    @Delete("delete from investor_workspace.t_sector where sctr_pk = #{id};\n" +
            "commit;")
    void deleteSector(int id);

    @Update("update investor_workspace.t_sector set sctr_name = #{sector.sctrName} where sctr_pk = #{id};\n" +
            "commit;")
    void updateSector(@Param("id") int id, @Param("sector") Sector sector);
}
