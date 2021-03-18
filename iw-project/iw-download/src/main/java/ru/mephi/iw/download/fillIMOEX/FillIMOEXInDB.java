package ru.mephi.iw.download.fillIMOEX;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.association.PriceStockInIndexMapper;
import ru.mephi.iw.dao.mappers.stocks.DoicMapper;
import ru.mephi.iw.dao.mappers.stocks.SiiMapper;
import ru.mephi.iw.dao.mappers.stocks.SpMapper;
import ru.mephi.iw.dao.mappers.stocks.StockMapper;
import ru.mephi.iw.dao.upsertIndexes.WriteIMOEXInDB;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.associations.PriceStockInIndex;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;

public class FillIMOEXInDB {

    private FillIMOEXInDB() {
    }

    public static boolean fill(boolean needCheckPrice, File uploadFile) {

        List<PriceStockInIndex> priceStockInIndicesOld;

        File xlsxFile;
        if (uploadFile == null) {
            xlsxFile = new File(System.getProperty("user.home") + "\\Downloads\\Picture_With_IMOEX_Data.xlsx");
        } else {
            xlsxFile = uploadFile;
        }

        try (SqlSession sqlSession = Initial.getSqlSessionFactory().openSession()) {

            SiiMapper siiMapper = sqlSession.getMapper(SiiMapper.class);
            DoicMapper doicMapper = sqlSession.getMapper(DoicMapper.class);
            StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
            SpMapper spMapper = sqlSession.getMapper(SpMapper.class);

            PriceStockInIndexMapper psi = sqlSession.getMapper(PriceStockInIndexMapper.class);
            priceStockInIndicesOld = psi.selectLastStocksPricesForIndex(1);

            if (priceStockInIndicesOld.size() != 0) {

                Calendar dateInDB = Calendar.getInstance();
                dateInDB.setTime(priceStockInIndicesOld.get(0).getStockPrice().getSettingTime());
                Calendar dateNow = Calendar.getInstance();
                dateNow.add(Calendar.DATE, -1);

                if (dateNow.get(Calendar.MONTH) == dateInDB.get(Calendar.MONTH) &&
                        dateNow.get(Calendar.DAY_OF_MONTH) == dateInDB.get(Calendar.DAY_OF_MONTH) &&
                        dateNow.get(Calendar.YEAR) == dateInDB.get(Calendar.YEAR)) {
                    return false;
                }
            }

            if (!xlsxFile.exists()) {
                xlsxFile = DownloadXlsIMOEXFile.convertGifToXlsx(DownloadIMOEXPicture.downloadIndexGif());
            }

            List<PriceStockInIndex> priceStockInIndices;
            try {
                priceStockInIndices = ReadIMOEXFromFile.readXlsx(xlsxFile);
            } catch (Exception ex) {
                throw new IwRuntimeException("Не получается считать файл " + System.getProperty("user.home") +
                        "\\Downloads\\" + xlsxFile.getName() + " : ", ex);
            }
            WriteIMOEXInDB.insertIndexData(priceStockInIndices, priceStockInIndicesOld, doicMapper, siiMapper, stockMapper,
                    spMapper, needCheckPrice);
        }
        System.out.println("Данные IMOEX успешно загружены в базу");
        if (!xlsxFile.delete()){
            throw new IwRuntimeException("Не могу удалить скачанный файл " + System.getProperty("user.home") +
                    "\\Downloads\\" + xlsxFile.getName());
        }
        return true;
    }
}
