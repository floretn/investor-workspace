package ru.mephi.iw.dao.test;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.association.PriceStockInIndexMapper;
import ru.mephi.iw.models.associations.PriceStockInIndex;

import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        try (SqlSession sqlSession = Initial.getSqlSessionFactory().openSession()) {
            //sqlSession.selectList("selectAllForIMOEX");
            List<PriceStockInIndex> allForIMOEX = sqlSession.getMapper(PriceStockInIndexMapper.class).
                    selectLastStocksPricesForIndex(1);
            System.out.println(allForIMOEX.get(0).toString());
        }
    }
}
