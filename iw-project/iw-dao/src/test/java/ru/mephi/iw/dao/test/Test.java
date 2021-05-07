package ru.mephi.iw.dao.test;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.stocks.DoicMapper;
import ru.mephi.iw.dao.mappers.stocks.SiiMapper;
import ru.mephi.iw.dao.mappers.stocks.SpMapper;
import ru.mephi.iw.dao.mappers.stocks.StockMapper;
import ru.mephi.iw.dao.mappers.stocks.association.PriceStockInIndexMapper;
import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksInIndexes;
import ru.mephi.iw.models.stocks.StocksPrices;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {

            DateOfIndexesChanges doic = new DateOfIndexesChanges(0, 1, LocalDate.now());
            sqlSession.getMapper(DoicMapper.class).insertDoic(doic);
            System.out.println(doic);

            StocksInIndexes stocksInIndexes = new StocksInIndexes(0, 1, 1, 1);
            sqlSession.getMapper(SiiMapper.class).insertSii(stocksInIndexes);
            System.out.println(stocksInIndexes);

            StocksPrices stocksPrices = new StocksPrices(0, 1, 1, new Timestamp(System.currentTimeMillis()), 123.0);
            sqlSession.getMapper(SpMapper.class).insertSp(stocksPrices);
            System.out.println(stocksPrices);

            Stock stock = new Stock(0, null, null, "Ticker");
            sqlSession.getMapper(StockMapper.class).insertStock(stock);
            System.out.println(stock);
            //System.out.println(sqlSession.getMapper(DoicMapper.class).select(doic.getId()));
            /*
            List<PriceStockInIndex> allForIMOEX = sqlSession.getMapper(PriceStockInIndexMapper.class).
                    selectLastStocksPricesForIndex(1);
            System.out.println(allForIMOEX);

            DateOfIndexesChanges dateOfIndexesChanges = new DateOfIndexesChanges(0, 1, LocalDate.now());
            sqlSession.getMapper(DoicMapper.class).insertDoic(dateOfIndexesChanges);
            System.out.println(dateOfIndexesChanges);

             */
        }
    }
}
