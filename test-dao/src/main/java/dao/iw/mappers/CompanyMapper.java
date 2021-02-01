package dao.iw.mappers;

import models.Company;
import org.apache.ibatis.annotations.*;

public interface CompanyMapper {

    @Select("select cmpn_pk, cmpn_name from investor_workspace.t_companies where cmpn_pk = #{id}")
    Company selectCompany(int id);

    @Insert("insert into investor_workspace.t_companies (cmpn_name)\n" +
            "values (#{company.cmpnName});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "company.cmpnPK", before = false, resultType = Integer.class)
    void insertCompany(@Param("company") Company company);

    @Delete("delete from investor_workspace.t_companies where cmpn_pk = #{id};\n" +
            "commit;")
    void deleteCompany(int id);

    @Update("update investor_workspace.t_companies set cmpn_name = #{company.cmpnName} where cmpn_pk = #{id};\n" +
            "commit;")
    void updateCompany(@Param("id") int id, @Param("company") Company company);
}