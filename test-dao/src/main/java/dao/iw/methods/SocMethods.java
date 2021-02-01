package dao.iw.methods;

import dao.iw.mappers.SocMapper;
import models.SectorsOfCompanies;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class SocMethods {

    private static volatile SqlSession sqlSession;
    private static volatile SocMapper mapper;

    public SocMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(SocMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(SocMapper.class);
        sqlSession = session;
    }

    public List<SectorsOfCompanies> showAllSoc(){
        return sqlSession.selectList("selectAllSoc");
    }

    public SectorsOfCompanies selectSoc(int id){
        return mapper.selectSoc(id);
    }

    public void insertSoc(SectorsOfCompanies soc){
        mapper.insertSoc(soc);
    }

    public void deleteSoc(int id){
        mapper.deleteSoc(id);
    }

    public void updateSoc(int id, SectorsOfCompanies soc){
        mapper.updateSoc(id, soc);
    }
}
