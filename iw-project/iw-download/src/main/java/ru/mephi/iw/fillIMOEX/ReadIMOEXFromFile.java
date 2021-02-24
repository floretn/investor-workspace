package ru.mephi.iw.fillIMOEX;

import ru.mephi.iw.models.Stock;
import ru.mephi.iw.models.StocksInIndexes;
import ru.mephi.iw.models.StocksPrices;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ReadIMOEXFromFile {

    static List<Stock> stocks;
    static List<StocksPrices> stocksPrices;
    static List<StocksInIndexes> stocksInIndexes;

    private static Iterator<Cell> iterator;
    private static boolean check = true;
    private static String ticker;
    private static String priceS;
    private static String capStckInIndS;
    private static int stckNum = 1;
    private static boolean checkFirstTime = true;

    ReadIMOEXFromFile() {
    }

    static void readXls(InputStream is) throws IOException {
        try(XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row cells : sheet) {

                if (checkFirstTime) {
                    checkFirstTime = false;
                    continue;
                }

                if (check) {

                    ifLineWithTwoElements(cells);

                    if (stckNum == 46) {
                        stckNum = 1;
                        break;
                    }
                    continue;
                }

                ifLineWithOneElement(cells);

                if (stckNum == 44){
                    check = true;
                }

            }
        }
    }

    private static void ifLineWithTwoElements(Row cells) {
        iterator = cells.cellIterator();

        ticker = iterator.next().getStringCellValue();
        String tickerSecond = ticker.substring(ticker.lastIndexOf("\n") + 1);
        ticker = ticker.substring(0, ticker.lastIndexOf("\n"));

        priceS = iterator.next().getStringCellValue();
        String priceSSecond = priceS.substring(priceS.lastIndexOf("\n") + 1);
        priceS = priceS.substring(0, priceS.lastIndexOf("\n"));

        Cell cell = iterator.next();
        String numStckFirst;
        String numStckSecond;
        try {
            numStckFirst = cell.getStringCellValue();
            numStckSecond = numStckFirst.substring(numStckFirst.lastIndexOf("\n") + 1);
            numStckFirst = numStckFirst.substring(0, numStckFirst.lastIndexOf("\n"));
        }catch (java.lang.IllegalStateException ex){
            int d = (int) cell.getNumericCellValue();
            numStckFirst = d + "";
            numStckSecond = "";
        }
        String s = iterator.next().getStringCellValue();
        String ss = s.substring(s.lastIndexOf("\n") + 1);
        s = s.substring(0, s.lastIndexOf("\n"));
        numStckFirst += s;
        numStckSecond += ss;

        iteratorNext(3, iterator);

        capStckInIndS = iterator.next().getStringCellValue();
        String capStckInIndSSecond = capStckInIndS.substring(capStckInIndS.lastIndexOf("\n") + 1);
        capStckInIndS = capStckInIndS.substring(0, capStckInIndS.lastIndexOf("\n"));

        check = false;
        if (stckNum == 1) {
            stocks = new ArrayList<>(45);
            stocksPrices = new ArrayList<>(45);
            stocksInIndexes = new ArrayList<>(45);
        }

        formLists(ticker, priceS, capStckInIndS);
        formLists(tickerSecond, priceSSecond, capStckInIndSSecond);
        stckNum += 2;
    }

    private static void ifLineWithOneElement(Row cells) {

        iterator = cells.cellIterator();
        //iterator.next();
        ticker = iterator.next().getStringCellValue();

        Cell cell = iterator.next();
        try {
            priceS = cell.getStringCellValue();
        } catch (java.lang.IllegalStateException ex) {
            double price = cell.getNumericCellValue();
            priceS = price + "";
        }

        long numStck = readNumStckAll(iterator);

        iteratorNext(2, iterator);

        String dop = "";
        try {
            String s = iterator.next().getStringCellValue();
            if (s.contains(" ")){
                dop = s.substring(s.lastIndexOf(" "));
            }
        }catch (Exception ex){
        }

        cell = iterator.next();
        try {
            capStckInIndS = cell.getStringCellValue();
        } catch (java.lang.IllegalStateException ex) {
            double capStckInInd = cell.getNumericCellValue();
            capStckInIndS = capStckInInd + "";
        }

        capStckInIndS = dop.replaceAll(" ", "") + capStckInIndS;
        while (capStckInIndS.lastIndexOf(",") != capStckInIndS.indexOf(",")){
            capStckInIndS = capStckInIndS.replaceFirst(",", "");
        }

        formLists(ticker, priceS, capStckInIndS);
        stckNum++;
    }

    private static void iteratorNext(int i, Iterator<Cell> iterator){
        for (int j = 1; j <= i; j++){
            iterator.next();
        }
    }

    private static void formLists(String ticker, String priceS, String capStckInIndexS){
        stocks.add(Stock.builder().companyId(null).ticker(ticker).build());
        double price = Double.parseDouble(priceS.replaceAll(",", ".").replaceAll(" ", ""));
        stocksPrices.add(StocksPrices.builder().currencyId(1).
                settingTime(new Timestamp(System.currentTimeMillis())).price(price).build());
        double capStckInIndex = Double.parseDouble(capStckInIndexS.replaceAll(",,", ",").
                replaceAll(",", ".").replaceAll(" ", ""));
        long numStckInIndex = (long) (capStckInIndex / price);
        stocksInIndexes.add(StocksInIndexes.builder().dateOfIndexesChangesId(1).
                numberOfStocksInIndex(numStckInIndex).build());
    }

    private static long readNumStckAll(Iterator<Cell> iterator){
        Cell cell;
        long numStck = 0;
        long dop;
        boolean check = true;
        for (int i = 1; i <= 2; i++ ){
            cell = iterator.next();
            try{
                dop = (long) cell.getNumericCellValue();
            }catch (java.lang.IllegalStateException ex){
                continue;
            }
            if (check) {
                numStck = dop;
                check = false;
                continue;
            }
            numStck = (numStck * 1000) + dop;
        }
        return numStck;
    }
}
