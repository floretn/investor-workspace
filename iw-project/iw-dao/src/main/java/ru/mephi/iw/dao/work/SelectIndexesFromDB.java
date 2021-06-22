package ru.mephi.iw.dao.work;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.stocks.IwuMapper;
import ru.mephi.iw.dao.mappers.stocks.association.PriceStockInIndexMapper;
import ru.mephi.iw.models.stocks.IndexWasUpload;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class SelectIndexesFromDB {

    @AllArgsConstructor
    public static class IndexStruct{

        List<PriceStockInIndex> priceStockInIndicesBefore;
        List<PriceStockInIndex> priceStockInIndicesAfter;
        IndexWasUpload indexWasUpload;

        public List<PriceStockInIndex> getPriceStockInIndicesBefore() {
            return priceStockInIndicesBefore;
        }

        public List<PriceStockInIndex> getPriceStockInIndicesAfter() {
            return priceStockInIndicesAfter;
        }

        public IndexWasUpload getIndexWasUpload() {
            return indexWasUpload;
        }
    }

    public IndexStruct selectStructAfterBefore(int idIndex, Date dateForDownload) {

        List<PriceStockInIndex> priceStockInIndicesBefore;
        List<PriceStockInIndex> priceStockInIndicesAfter;
        IndexWasUpload indexWasUpload;

        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {

            Calendar calendarForDownload = Calendar.getInstance();
            calendarForDownload.setTime(dateForDownload);
            PriceStockInIndexMapper priceStockInIndexMapper = sqlSession.getMapper(PriceStockInIndexMapper.class);
            calendarForDownload.add(Calendar.DATE, +1);

            priceStockInIndicesBefore = priceStockInIndexMapper.selectLastStocksPricesForIndexBeforeDate(idIndex,
                    new Timestamp(dateForDownload.getTime()));

            priceStockInIndicesAfter = priceStockInIndexMapper.selectFirstStocksPricesForIndexAfterDate(idIndex,
                    new Timestamp(dateForDownload.getTime()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateForDownload);
            LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
                    , calendar.get(Calendar.DATE));
            indexWasUpload = sqlSession.getMapper(IwuMapper.class).selectIwu(idIndex, localDate);
        }
        return new IndexStruct(priceStockInIndicesBefore, priceStockInIndicesAfter, indexWasUpload);
    }
}
