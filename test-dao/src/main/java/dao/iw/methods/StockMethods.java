package dao.iw.methods;

import dao.iw.mappers.StockMapper;
import models.Stock;
import org.apache.ibatis.session.SqlSession;
import java.io.IOException;
import java.util.List;

public class StockMethods {

    private static volatile SqlSession sqlSession;
    private static volatile StockMapper mapper;

    public StockMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(StockMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(StockMapper.class);
        sqlSession = session;
    }

    public List<Stock> showAllStocks(){
        return sqlSession.selectList("selectAllStocks");
    }

    public Stock selectStock(int id){
        return mapper.selectStock(id);
    }

    public void insertStock(Stock stock){
        mapper.insertStock(stock);
    }

    public void deleteStock(int id){
        mapper.deleteStock(id);
    }

    public void updateStock(int id, Stock stock){
        mapper.updateStock(id, stock);
    }
}
