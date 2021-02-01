package dao.iw.methods;

import dao.iw.mappers.SpMapper;
import models.StocksPrices;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class SpMethods {

    private static volatile SqlSession sqlSession;
    private static volatile SpMapper mapper;

    public SpMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(SpMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(SpMapper.class);
        sqlSession = session;
    }

    public List<StocksPrices> showAllSp(){
        return sqlSession.selectList("selectAllSp");
    }

    public StocksPrices selectSp(int id){
        return mapper.selectSp(id);
    }

    public void insertSp(StocksPrices sp){
        mapper.insertSp(sp);
    }

    public void deleteSp(int id){
        mapper.deleteSp(id);
    }

    public void updateSp(int id, StocksPrices sp){
        mapper.updateSp(id, sp);
    }
}
