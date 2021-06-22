package ru.mephi.iw.dao.work;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.stocks.*;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import ru.mephi.iw.models.stocks.IndexWasUpload;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class UpsertIndexInDB {

    public void insertOnlyPrice(List<PriceStockInIndex> priceStockInIndices, DateOfIndexesChanges doicChange, int indexId) {
        IwRuntimeException exception = null;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                for (PriceStockInIndex priceStockInIndex : priceStockInIndices) {
                    priceStockInIndex.getStockPrice().setStockId(priceStockInIndex.getStock().getId());
                    sqlSession.getMapper(SpMapper.class).insertSp(priceStockInIndex.getStockPrice());
                }
                insertIwu(priceStockInIndices, sqlSession, indexId);

                if (doicChange != null) {
                    sqlSession.getMapper(DoicMapper.class).updateDoic(doicChange.getId(), doicChange);
                }
                sqlSession.commit();
            } catch (Exception ex) {
                try {
                    sqlSession.rollback();
                } catch (Exception ex1) {
                    ex.addSuppressed(ex1);
                }
                exception = new IwRuntimeException("Внутренняя ошибка!", ex);
            }
        }

        if (exception != null) {
            throw exception;
        }

    }

    public void ifIndexWasChanged(List<PriceStockInIndex> priceStockInIndices, int indexId) {

        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {

                DateOfIndexesChanges doicNew = new DateOfIndexesChanges(0, indexId,
                        priceStockInIndices.get(0).getStockPrice().getSettingTime().toLocalDateTime().toLocalDate());
                sqlSession.getMapper(DoicMapper.class).insertDoic(doicNew);

                for (PriceStockInIndex priceStockInIndex : priceStockInIndices) {
                    priceStockInIndex.setDateOfIndexChange(doicNew);

                    StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
                    Stock stock = stockMapper.selectStockByTicker(priceStockInIndex.getStock().getTicker());
                    if (stock == null) {
                        stockMapper.insertStock(priceStockInIndex.getStock());
                    } else {
                        priceStockInIndex.setStock(stock);
                    }

                    priceStockInIndex.getStockInIndex().setStockId(priceStockInIndex.getStock().getId());
                    priceStockInIndex.getStockInIndex().setDateOfIndexesChangesId(doicNew.getId());
                    sqlSession.getMapper(SiiMapper.class).insertSii(priceStockInIndex.getStockInIndex());

                    priceStockInIndex.getStockPrice().setStockId(priceStockInIndex.getStock().getId());
                    sqlSession.getMapper(SpMapper.class).insertSp(priceStockInIndex.getStockPrice());
                }
                insertIwu(priceStockInIndices, sqlSession, indexId);
                sqlSession.commit();
            } catch (Exception ex) {
                try {
                    sqlSession.rollback();
                } catch (Exception ex1) {
                    ex.addSuppressed(ex1);
                }
                throw ex;
            }
        }
    }

    private void insertIwu(List<PriceStockInIndex> priceStockInIndices, SqlSession sqlSession, int indexId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(priceStockInIndices.get(0).getStockPrice().getSettingTime().getTime()));
        LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
                , calendar.get(Calendar.DATE));
        sqlSession.getMapper(IwuMapper.class).insertIwu(new IndexWasUpload(0, indexId, localDate, true));
    }
}
