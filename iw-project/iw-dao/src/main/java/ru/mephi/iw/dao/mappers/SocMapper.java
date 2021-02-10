package ru.mephi.iw.dao.mappers;

import ru.mephi.iw.models.SectorsOfCompanies;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SocMapper {

    @Select("select soc_pk, soc_cmpn_fk, soc_sctr_fk from investor_workspace.t_sectors_of_companies where soc_pk = #{id}")
    SectorsOfCompanies selectSoc(int id);

    @Select("select soc_pk, soc_cmpn_fk, soc_sctr_fk from investor_workspace.t_sectors_of_companies where soc_pk = #{id}")
    List<SectorsOfCompanies> selectAllSoc();

    @Insert("insert into investor_workspace.t_sectors_of_companies (soc_cmpn_fk, soc_sctr_fk)\n" +
            "values (#{soc.companyId}, #{soc.sectorId});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "soc.id", before = false, resultType = Integer.class)
    void insertSoc(@Param("soc") SectorsOfCompanies soc);

    @Delete("delete from investor_workspace.t_sectors_of_companies where soc_pk = #{id};\n" +
            "commit;")
    void deleteSoc(int id);

    @Update("update investor_workspace.t_sectors_of_companies set soc_cmpn_fk = #{soc.companyId},\n" +
            "soc_sctr_fk = #{soc.sectorId} where soc_pk = #{id};\n" +
            "commit;")
    void updateSoc(@Param("id") int id, @Param("soc") SectorsOfCompanies soc);
}
