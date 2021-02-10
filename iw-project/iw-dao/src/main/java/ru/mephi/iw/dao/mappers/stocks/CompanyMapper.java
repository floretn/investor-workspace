package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.Company;
import org.apache.ibatis.annotations.*;

public interface CompanyMapper {

    @Select("select cmpn_pk, cmpn_name from investor_workspace.t_companies where cmpn_pk = #{id}")
    Company selectCompany(int id);

    @Insert("insert into investor_workspace.t_companies (cmpn_name)\n" +
            "values (#{company.name});\n" +
            "commit;")
    @SelectKey(statement = "SELECT LASTVAL()", keyProperty = "company.id", before = false, resultType = Integer.class)
    void insertCompanies(@Param("company") Company company);

    @Delete("delete from investor_workspace.t_companies where cmpn_pk = #{id};\n" +
            "commit;")
    void deleteCompany(int id);

    @Update("update investor_workspace.t_companies set cmpn_name = #{company.name} where cmpn_pk = #{id};\n" +
            "commit;")
    void updateCompany(@Param("id") int id, @Param("company") Company company);
}