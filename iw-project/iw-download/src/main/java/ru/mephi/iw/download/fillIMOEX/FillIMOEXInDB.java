package ru.mephi.iw.download.fillIMOEX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.iw.constants.IndexKeys;
import ru.mephi.iw.dao.work.SelectIndexesFromDB;
import ru.mephi.iw.dao.work.UpsertIndexInDB;
import ru.mephi.iw.download.FillIndexInDB;
import ru.mephi.iw.download.IndexChangesCalculator;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class FillIMOEXInDB implements FillIndexInDB {

    private final ReadIMOEXFromFile readIMOEXFromFile;
    private final DownloadIMOEXPicture downloadIMOEXPicture;
    private final DownloadXlsxIMOEXFile downloadXlsxIMOEXFile;
    private final IndexChangesCalculator indexChangesCalculator;
    private final UpsertIndexInDB upsertIndexInDB;
    private final SelectIndexesFromDB selectIndexesFromDB;

    @Autowired
    public FillIMOEXInDB(ReadIMOEXFromFile readIMOEXFromFile, DownloadIMOEXPicture downloadIMOEXPicture,
                         DownloadXlsxIMOEXFile downloadXlsxIMOEXFile, IndexChangesCalculator indexChangesCalculator,
                         UpsertIndexInDB upsertIndexInDB, SelectIndexesFromDB selectIndexesFromDB) {
        this.readIMOEXFromFile = readIMOEXFromFile;
        this.downloadIMOEXPicture = downloadIMOEXPicture;
        this.downloadXlsxIMOEXFile = downloadXlsxIMOEXFile;
        this.indexChangesCalculator = indexChangesCalculator;
        this.upsertIndexInDB = upsertIndexInDB;
        this.selectIndexesFromDB = selectIndexesFromDB;
    }

    /**
     * @param trustInfo - флаг показывающий нужно ли проверять информацию в файле, или данные можно считать верными и записывать в базу.
     * @param dateForDownload - дата для загрузки индекса.
     * @param fileWithIndex - файл, который загружен пользователем. Если переменная равна null, то делается скачивание файла автоматически.
     * Иначе парсится полученный файл.
     * @return boolean - true если данные были загружены в базу, false - если данные за дату dateForDownload уже были загружены в систему.
     */
    @Override
    public boolean fill(boolean trustInfo, Date dateForDownload, File fileWithIndex) {

        Calendar calendarForDownload = Calendar.getInstance();
        calendarForDownload.setTime(dateForDownload);

        Calendar currentCalendar = Calendar.getInstance();

        File xlsxFile;
        if (fileWithIndex == null) {
            xlsxFile = new File(System.getProperty("user.home") + "\\Downloads\\Picture_With_IMOEX_Data" +
                    (new SimpleDateFormat("yyyyMMdd")).format(calendarForDownload.getTime()) + ".xlsx");
        } else {
            xlsxFile = fileWithIndex;
        }

        if ((dateForDownload.compareTo(new Date()) > 0 ||
                (calendarForDownload.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) &&
                 calendarForDownload.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                 calendarForDownload.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH))) {
            throw new IwRuntimeException("Это либо сегодняшний, либо будущий день. Данных на указанную дату пока нет.");
        }

        if ((calendarForDownload.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                calendarForDownload.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            throw new IwRuntimeException("Это был выходной день, данные на сайте Мос. Биржи не обновлялись.");
        }

        SelectIndexesFromDB.IndexStruct imoexStruct = selectIndexesFromDB.selectStructAfterBefore(IndexKeys.IMOEX_KEY, dateForDownload);

        if (imoexStruct.getIndexWasUpload() != null) {
            return false;
        }

        if (!xlsxFile.exists()) {
            xlsxFile = downloadXlsxIMOEXFile.convertGifToXlsx(downloadIMOEXPicture.downloadIndexGif(dateForDownload));
            File renameTo = new File(System.getProperty("user.home") +
                    "\\Downloads\\Picture_With_IMOEX_Data" + (new SimpleDateFormat("yyyyMMdd")).
                    format(calendarForDownload.getTime()) + ".xlsx");
            if (!(xlsxFile.renameTo(renameTo))) {
                throw new IwRuntimeException("Не могу получить доступ к файлу " + xlsxFile.getAbsolutePath());
            }
            xlsxFile = renameTo;
        }

        List<PriceStockInIndex> priceStockInIndices;
        try {
            calendarForDownload.add(Calendar.SECOND, 67800);
            priceStockInIndices = readIMOEXFromFile.readXlsx(xlsxFile, new Timestamp(calendarForDownload.getTimeInMillis()));
        } catch (IwRuntimeException ire) {
            throw ire;
        } catch (Exception e) {
            throw new IwRuntimeException("Не получается считать файл " + xlsxFile.getAbsolutePath() + " : ", e);
        }

        IndexChangesCalculator.NewIndexStruct newIndexStruct = indexChangesCalculator.checkIndexData(priceStockInIndices,
                imoexStruct, trustInfo);

        if (newIndexStruct.isIndexWasChanges()) {
            priceStockInIndices = newIndexStruct.getPriceStockInIndicesNew();
            upsertIndexInDB.ifIndexWasChanged(priceStockInIndices, IndexKeys.IMOEX_KEY);
        } else {
            upsertIndexInDB.insertOnlyPrice(priceStockInIndices, newIndexStruct.getDoicChange(), IndexKeys.IMOEX_KEY);
        }

        System.out.println("Данные IMOEX успешно загружены в базу");
        deleteFiles(calendarForDownload, fileWithIndex, xlsxFile);
        return true;
    }

    void deleteFiles(Calendar calendarForDownload, File fileWithIndex, File xlsxFile) {
        IwRuntimeException exDelete = null;
        if (!xlsxFile.delete()){
            exDelete = new IwRuntimeException("Индекс был успешно загружен в систему, но не могу удалить скачанный файл " +
                    xlsxFile.getAbsolutePath());
        }

        if (fileWithIndex != null) {
            File fileOld = new File(System.getProperty("user.home") + "\\Downloads\\Picture_With_IMOEX_Data" +
                    (new SimpleDateFormat("yyyyMMdd")).format(calendarForDownload.getTime()) + ".xlsx");
            if (fileOld.exists() && !fileOld.delete()) {
                if (exDelete != null) {
                    exDelete.addSuppressed(new IwRuntimeException("Индекс был успешно загружен в систему, " +
                            "но не могу удалить скачанный файл " + fileOld.getAbsolutePath()));
                    throw exDelete;
                }
                exDelete = new IwRuntimeException("Индекс был успешно загружен в систему, " +
                        "но не могу удалить скачанный файл " + fileOld.getAbsolutePath());
            }
        }

        if (exDelete != null) {
            throw exDelete;
        }
    }
}
