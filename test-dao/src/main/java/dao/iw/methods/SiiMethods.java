package dao.iw.methods;

import dao.iw.mappers.SiiMapper;
import models.StocksInIndexes;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class SiiMethods {

    private static volatile SqlSession sqlSession;
    private static volatile SiiMapper mapper;

    public SiiMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(SiiMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(SiiMapper.class);
        sqlSession = session;
    }

    public List<StocksInIndexes> showAllSii(){
        return sqlSession.selectList("selectAllSii");
    }

    public StocksInIndexes selectSii(int id){
        return mapper.selectSii(id);
    }

    public void insertSii(StocksInIndexes StocksInIndexes){
        mapper.insertSii(StocksInIndexes);
    }

    public void deleteSii(int id){
        mapper.deleteSii(id);
    }

    public void updateSii(int id, StocksInIndexes StocksInIndexes){
        mapper.updateSii(id, StocksInIndexes);
    }
}
