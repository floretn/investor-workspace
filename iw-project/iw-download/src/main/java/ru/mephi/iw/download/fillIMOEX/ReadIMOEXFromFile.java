package ru.mephi.iw.download.fillIMOEX;

import javafx.util.Pair;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.associations.PriceStockInIndex;
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

public class ReadIMOEXFromFile {

    /**Количсество колонок, которые нужно пропустить после считывания цены, чтобы начать считывать капитализацию*/
    private static final int NUM_COLUMNS_BETWEEN_PRICE_AND_CAP = 5;

    private ReadIMOEXFromFile() {
    }

    static List<PriceStockInIndex> readXlsx(File xlsxFile) throws IOException {

        List<PriceStockInIndex> stocksAndPricesIMOEX = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        try(XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsxFile))) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            String checkS;
            int numColumnToMiss = 0;

            for (Row cells : sheet) {

                /*Таблица может начинаться либо с первой, либо со второй колонки.
                * В первом блоке try пытаюсь считать ячейку в первой колонке, в блоке catch пытаюсь считать вторую колонку.
                * Если обе колонки не считываются - значит таблица начинается и не с первой строки.
                * В этом случае переход к следующей итерации цикла - то есть к следующей строке.
                * Плюсом - запоминаю необходимое количество итерация для использования в методах в переменную columnNumberWhereTableStart.
                */
                try {
                    checkS = cells.getCell(0).getStringCellValue();
                    if (checkS.equals("Nei Namibia")){
                        throw new IwRuntimeException("Данные индекса не были обновлены на сайте Московской Биржи...");
                    }
                } catch (java.lang.IllegalStateException ex) {
                    try {
                        checkS = cells.getCell(1).getStringCellValue();
                        numColumnToMiss = 1;
                    } catch (java.lang.IllegalStateException ex1) {
                        continue;
                    }
                }

                if (checkS.isEmpty() || checkS.length() <= 3) {
                    continue;
                }

                if (checkS.contains("\n")) {

                    Pair<PriceStockInIndex, PriceStockInIndex> pair =
                            ifLineWithTwoElements(numColumnToMiss, cells, timestamp);
                    stocksAndPricesIMOEX.add(pair.getKey());
                    stocksAndPricesIMOEX.add(pair.getValue());

                    continue;
                }

                stocksAndPricesIMOEX.add(ifLineWithOneElement(numColumnToMiss, cells, timestamp));

            }
        }
        return stocksAndPricesIMOEX;
    }

    private static Pair<PriceStockInIndex, PriceStockInIndex> ifLineWithTwoElements
                                            (int numColumnToMiss, Row cells, Timestamp timestamp) {

        Iterator<Cell> iterator = cells.cellIterator();
        iteratorNext(numColumnToMiss, iterator);

        String ticker = iterator.next().getStringCellValue();
        String tickerSecond = ticker.substring(ticker.lastIndexOf("\n") + 1);
        ticker = ticker.substring(0, ticker.lastIndexOf("\n"));

        String priceS = iterator.next().getStringCellValue();
        String priceSSecond = priceS.substring(priceS.lastIndexOf("\n") + 1);
        priceS = priceS.substring(0, priceS.lastIndexOf("\n"));


        iteratorNext(NUM_COLUMNS_BETWEEN_PRICE_AND_CAP, iterator);

        /*Если капитализация в индексе слишком большая, то она распадается на две ячейки.
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
        }

        String capStckInIndS = iterator.next().getStringCellValue();
        String capStckInIndSSecond = capStckInIndS.substring(capStckInIndS.lastIndexOf("\n") + 1);
        capStckInIndS = capStckInIndS.substring(0, capStckInIndS.lastIndexOf("\n"));

        return new Pair<>(formPriceStockInIndexForCertainDate(ticker, priceS, capStckInIndS, dop, timestamp),
                formPriceStockInIndexForCertainDate(tickerSecond, priceSSecond, capStckInIndSSecond, dopSecond, timestamp));

    }

    private static PriceStockInIndex formPriceStockInIndexForCertainDate(String ticker, String priceS,
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

        return new PriceStockInIndex(0, null,
                StocksInIndexes.builder().dateOfIndexesChangesId(1).numberOfStocksInIndex(numStckInIndex).build(),
                Stock.builder().companyId(null).ticker(ticker).build(), StocksPrices.builder().currencyId(1).
                settingTime(timestamp).price(price).build());
    }

    private static PriceStockInIndex ifLineWithOneElement(int numColumnToMiss, Row cells, Timestamp timestamp) {

        Iterator<Cell> iterator = cells.cellIterator();
        iteratorNext(numColumnToMiss, iterator);

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

    private static void iteratorNext(int i, Iterator<Cell> iterator){
        for (int j = 1; j <= i; j++){
            iterator.next();
        }
    }
}
