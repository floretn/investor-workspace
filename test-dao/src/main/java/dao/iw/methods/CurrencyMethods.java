package dao.iw.methods;

import dao.iw.mappers.CurrencyMapper;
import models.Currency;
import org.apache.ibatis.session.SqlSession;
import java.io.IOException;
import java.util.List;

public class CurrencyMethods {

    private static volatile SqlSession sqlSession;
    private static volatile CurrencyMapper mapper;

    public CurrencyMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(CurrencyMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        //InitClass.sqlSessionFactory.getConfiguration().addMapper(CurrencyMapper.class);
        mapper = session.getMapper(CurrencyMapper.class);
        sqlSession = session;
    }

    public List<Currency> showAllCurrencies(){
        return sqlSession.selectList("selectAllCurrencies");
    }

    public Currency selectCurrency(int id){
        return mapper.selectCurrency(id);
    }

    public void insertCurrency(Currency Currency){
        mapper.insertCurrency(Currency);
    }

    public void deleteCurrency(int id){
        mapper.deleteCurrency(id);
    }

    public void updateCurrency(int id, Currency Currency){
        mapper.updateCurrency(id, Currency);
    }
}
