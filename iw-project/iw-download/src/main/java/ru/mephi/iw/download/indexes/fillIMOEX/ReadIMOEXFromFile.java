package ru.mephi.iw.download.indexes.fillIMOEX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.mephi.iw.constants.CurrencyKeys;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksInIndexes;
import ru.mephi.iw.models.stocks.StocksPrices;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@Component
class ReadIMOEXFromFile {

    /**Количсество колонок, которые нужно пропустить после считывания цены, чтобы начать считывать капитализацию*/
    private static final int NUM_COLUMNS_BETWEEN_PRICE_AND_CAP = 5;
    /**Количество без одной колонок в таблице*/
    private static final int NUM_COLUMNS_IN_TABLE = 9;
    /**
     * Логгер. Предполагается, что конфигурация для логгирования предоставляется сервером приложений.
     * Например на сервере Tomcat должен лежать файл logback.xml с конфигурацией в папке %TOMCAT_HOME%/lib
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadIMOEXFromFile.class);

    public List<PriceStockInIndex> readXlsx(File xlsxFile, Timestamp timeSet) throws IOException {

        LOGGER.info("Начинаю чтение xlsx файла по адресу " + xlsxFile.getAbsolutePath());
        
        List<PriceStockInIndex> stocksAndPricesIMOEX = new ArrayList<>();

        try(XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsxFile))) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            String checkS;

            for (Row cells : sheet) {

                /* Таблица может начинаться либо с первой, либо со второй колонки.
                 * В первом блоке try пытаюсь считать ячейку в первой колонке, в блоке catch пытаюсь считать вторую колонку.
                 * Если обе колонки не считываются - значит таблица начинается и не с первой строки.
                 * В этом случае переход к следующей итерации цикла - то есть к следующей строке.
                 * Плюсом - запоминаю необходимое количество итерация для использования в методах в переменную columnNumberWhereTableStart.
                 */
                try {
                    checkS = cells.iterator().next().getStringCellValue();
                    if (checkS.equals("Nei Namibia")){
                        throw new IwRuntimeException("Данные индекса не были обновлены на сайте Московской Биржи в этот день...");
                    }
                    /*
                     * Если не могу прочитать 9 столбцов подряд => эта строка не относится к таблице с данными.
                     */
                    iteratorNext(NUM_COLUMNS_IN_TABLE, cells.cellIterator());
                } catch (Exception ex) {
                    continue;
                }

                if (checkS.isEmpty() || checkS.length() < 3 || checkS.equals("\\PL+")) {
                    continue;
                }

                if (checkS.contains("\n")) {
                    Pair<PriceStockInIndex, PriceStockInIndex> pair =
                            ifLineWithTwoElements(cells, timeSet);
                    stocksAndPricesIMOEX.add(pair.getKey());
                    stocksAndPricesIMOEX.add(pair.getValue());
                    continue;
                }

                stocksAndPricesIMOEX.add(ifLineWithOneElement(cells, timeSet));

            }
        }
        return stocksAndPricesIMOEX;
    }

    private Pair<PriceStockInIndex, PriceStockInIndex> ifLineWithTwoElements (Row cells,
                                                                                     Timestamp timestamp) {

        LOGGER.info("Считываю линию с двумя элементами");
        
        Iterator<Cell> iterator = cells.cellIterator();

        String ticker = iterator.next().getStringCellValue();
        String tickerSecond = ticker.substring(ticker.lastIndexOf("\n") + 1);
        ticker = ticker.substring(0, ticker.lastIndexOf("\n"));

        String priceS = iterator.next().getStringCellValue();
        String priceSSecond = priceS.substring(priceS.lastIndexOf("\n") + 1);
        priceS = priceS.substring(0, priceS.lastIndexOf("\n"));


        iteratorNext(NUM_COLUMNS_BETWEEN_PRICE_AND_CAP, iterator);

        /* Если капитализация в индексе слишком большая, то она распадается на две ячейки.
         * Для считывания информации из первой ячейки предназначена переменная dop
         */
        String dop = "";
        String dopSecond = "";
        try {
            String s = iterator.next().getStringCellValue();
            String sSecond = s.substring(dop.lastIndexOf("\n"));
            s = s.substring(0, s.lastIndexOf("\n" + 1));
            if (sSecond.contains(" ")) {
                dopSecond = sSecond.substring(sSecond.lastIndexOf(" "));
            }

            if(s.contains(" ")) {
                dop = s.substring(s.lastIndexOf(" "));
            }
        }catch (Exception ex){
            /* Слишком большое число для капитализации распадается на две ячейки таблицы и попадает в первую из этих ячеек через пробел
             * от коэффициента, ограничивающего вес акции в индексе.
             * Если первую из двух ячеек не получается считать как строку => в ней не содержится числа, относящегося к капитализации.
             * Такая ошибка игнорируется.
             */
        }

        String capStckInIndS = iterator.next().getStringCellValue();
        String capStckInIndSSecond = capStckInIndS.substring(capStckInIndS.lastIndexOf("\n") + 1);
        capStckInIndS = capStckInIndS.substring(0, capStckInIndS.lastIndexOf("\n"));

        return Pair.of(formPriceStockInIndexForCertainDate(ticker, priceS, capStckInIndS, dop, timestamp),
                formPriceStockInIndexForCertainDate(tickerSecond, priceSSecond, capStckInIndSSecond, dopSecond, timestamp));

    }

    private PriceStockInIndex formPriceStockInIndexForCertainDate(String ticker, String priceS,
                                                                         String capStckInIndS, String dop, Timestamp timestamp) {
        capStckInIndS = dop + capStckInIndS;
        capStckInIndS = capStckInIndS.replaceAll(":", ",");
        if (capStckInIndS.contains(",") || capStckInIndS.contains(":")) {
            capStckInIndS = capStckInIndS.replaceAll(capStckInIndS.substring(0,
                    capStckInIndS.lastIndexOf(",")),
                    capStckInIndS.substring(0, capStckInIndS.lastIndexOf(",")).replaceAll(",", ""));
        }

        double capStckInIndex = Double.parseDouble(capStckInIndS.replaceAll(",,", ",").
                replaceAll(",", ".").replaceAll(" ", ""));
        double price = Double.parseDouble(priceS.replaceAll(",", ".").replaceAll(" ", ""));
        long numStckInIndex = (long) (capStckInIndex / price);
        
        PriceStockInIndex priceStockInIndex = new PriceStockInIndex(0, null,
                StocksInIndexes.builder().numberOfStocksInIndex(numStckInIndex).build(),
                Stock.builder().companyId(null).ticker(ticker).build(), StocksPrices.builder().currencyId(CurrencyKeys.RUBLES_KEY).
                settingTime(timestamp).price(price).build());

        LOGGER.info("Считал строку из файла: " + priceStockInIndex);
        
        return priceStockInIndex;
    }

    private PriceStockInIndex ifLineWithOneElement(Row cells, Timestamp timestamp) {
        
        LOGGER.info("Считываю линию с одним элементом");

        Iterator<Cell> iterator = cells.cellIterator();

        String ticker = iterator.next().getStringCellValue();

        Cell cell = iterator.next();

        String priceS;
        try {
            priceS = cell.getStringCellValue();
        } catch (java.lang.IllegalStateException ex) {
            double price = cell.getNumericCellValue();
            priceS = price + "";
        }

        iteratorNext(NUM_COLUMNS_BETWEEN_PRICE_AND_CAP, iterator);

        /*Если капитализация в индексе слишком большая, то она распадается на две ячейки.
        * Для считывания информации из первой ячейки предназначена переменная dop
        */
        String dop = "";
        try {
            String s = iterator.next().getStringCellValue();
            if (s.contains(" ")){
                dop = s.substring(s.lastIndexOf(" "));
            }
        }catch (Exception ex){
            /* Слишком большое число для капитализации распадается на две ячейки таблицы и попадает в первую из этих ячеек через пробел
             * от коэффициента, ограничивающего вес акции в индексе.
             * Если первую из двух ячеек не получается считать как строку => в ней не содержится числа, относящегося к капитализации.
             * Такая ошибка игнорируется.
             */
        }

        cell = iterator.next();
        String capStckInIndS;
        try {
            capStckInIndS = cell.getStringCellValue();
        } catch (java.lang.IllegalStateException ex) {
            double capStckInInd = cell.getNumericCellValue();
            capStckInIndS = capStckInInd + "";
        }
        return formPriceStockInIndexForCertainDate(ticker, priceS, capStckInIndS, dop, timestamp);
    }

    private void iteratorNext(int i, Iterator<Cell> iterator){
        for (int j = 1; j <= i; j++){
            iterator.next();
        }
    }
}
