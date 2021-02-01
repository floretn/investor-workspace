package dao.iw.methods;

import dao.iw.mappers.CompanyMapper;
import models.Company;
import org.apache.ibatis.session.SqlSession;
import java.io.IOException;
import java.util.List;

public class CompanyMethods {

    private static volatile SqlSession sqlSession;
    private static volatile CompanyMapper mapper;

    public CompanyMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(CompanyMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(CompanyMapper.class);
        sqlSession = session;
    }

    public List<Company> showAllCompanies(){
        return sqlSession.selectList("selectAllCompanies");
    }

    public Company selectCompany(int id){
        return mapper.selectCompany(id);
    }

    public void insertCompany(Company company){
        mapper.insertCompany(company);
    }

    public void deleteCompany(int id){
        mapper.deleteCompany(id);
    }

    public void updateCompany(int id, Company company){
        mapper.updateCompany(id, company);
    }
}

