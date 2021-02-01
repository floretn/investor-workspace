package dao.iw.mappers;

import models.SectorsOfCompanies;
import org.apache.ibatis.annotations.*;

public interface SocMapper {

    @Select("select soc_pk, soc_cmpn_fk, soc_sctr_fk from investor_workspace.t_sectors_of_companies where soc_pk = #{id}")
    SectorsOfCompanies selectSoc(int id);

    @Insert("insert into investor_workspace.t_sectors_of_companies (soc_cmpn_fk, soc_sctr_fk)\n" +
            "values (#{soc.socCmpnFK}, #{soc.socSctrFK});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "soc.socPK", before = false, resultType = Integer.class)
    void insertSoc(@Param("soc") SectorsOfCompanies soc);

    @Delete("delete from investor_workspace.t_sectors_of_companies where soc_pk = #{id};\n" +
            "commit;")
    void deleteSoc(int id);

    @Update("update investor_workspace.t_sectors_of_companies set soc_cmpn_fk = #{soc.socCmpnFK},\n" +
            "soc_sctr_fk = #{soc.socSctrFK} where soc_pk = #{id};\n" +
            "commit;")
    void updateSoc(@Param("id") int id, @Param("soc") SectorsOfCompanies soc);
}
