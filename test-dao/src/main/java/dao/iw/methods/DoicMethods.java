package dao.iw.methods;

import dao.iw.mappers.DoicMapper;
import models.DateOfIndexesChanges;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class DoicMethods {

    private static volatile SqlSession sqlSession;
    private static volatile DoicMapper mapper;

    public DoicMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(DoicMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(DoicMapper.class);
        sqlSession = session;
    }

    public List<DateOfIndexesChanges> showAllDoic(){
        return sqlSession.selectList("selectAllDoic");
    }

    public DateOfIndexesChanges selectDoic(int id){
        return mapper.selectDoic(id);
    }

    public void insertDoic(DateOfIndexesChanges doic){
        mapper.insertDoic(doic);
    }

    public void deleteDoic(int id){
        mapper.deleteDoic(id);
    }

    public void updateDoic(int id, DateOfIndexesChanges doic){
        mapper.updateDoic(id, doic);
    }
}
