package ru.mephi.iw.dao.test;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.stocks.SpMapper;
import ru.mephi.iw.dao.mappers.stocks.StockMapper;

public class TestStocks {

    public static void main(String[] args) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            System.out.println(sqlSession.getMapper(StockMapper.class).selectStockByTicker("AFKS"));
        }
    }
}
