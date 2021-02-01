package fillIMOEX;


import models.Stock;
import models.StocksInIndexes;
import models.StocksPrices;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ReadIMOEXClass {

    static List<Stock> stocks;
    static List<StocksPrices> stocksPrices;
    static List<StocksInIndexes> stocksInIndexes;

    ReadIMOEXClass() {
    }

    static void readXls(InputStream is) throws IOException {
        try(XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Cell> iterator;
            boolean check = true;
            String ticker;
            String tickerSecond;
            double price;
            String priceS;
            String priceSSecond;
            long numStck;
            String numStckFirst;
            String numStckSecond;
            String capStckInIndS;
            String capStckInIndSSecond;
            double capStckInInd;
            int stckPK = 1;

            for (Row cells : sheet) {

                if (check) {
                    iterator = cells.cellIterator();

                    iterator.next();
                    ticker = iterator.next().getStringCellValue();
                    tickerSecond = ticker.substring(ticker.lastIndexOf("\n") + 1);
                    ticker = ticker.substring(0, ticker.lastIndexOf("\n"));

                    priceS = iterator.next().getStringCellValue();
                    priceSSecond = priceS.substring(priceS.lastIndexOf("\n") + 1);
                    priceS = priceS.substring(0, priceS.lastIndexOf("\n"));

                    Cell cell = iterator.next();
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
                    capStckInIndSSecond = capStckInIndS.substring(capStckInIndS.lastIndexOf("\n") + 1);
                    capStckInIndS = capStckInIndS.substring(0, capStckInIndS.lastIndexOf("\n"));

                    check = false;
                    if (stckPK == 1) {
                        stocks = new ArrayList<>(45);
                        stocksPrices = new ArrayList<>(45);
                        stocksInIndexes = new ArrayList<>(45);
                    }

                    formLists(ticker, Long.parseLong(numStckFirst.replaceAll(" ", "")), priceS,
                            capStckInIndS, stckPK);
                    formLists(tickerSecond, Long.parseLong(numStckSecond.replaceAll(" ", "")), priceSSecond,
                            capStckInIndSSecond, stckPK++);
                    stckPK++;

                    if (stckPK == 46) {
                        break;
                    }
                    continue;
                }


                iterator = cells.cellIterator();
                iterator.next();
                ticker = iterator.next().getStringCellValue();

                Cell cell = iterator.next();
                try {
                    priceS = cell.getStringCellValue();
                } catch (java.lang.IllegalStateException ex) {
                    price = cell.getNumericCellValue();
                    priceS = price + "";
                }

                numStck = readNumStckAll(iterator);

                iteratorNext(3, iterator);
                cell = iterator.next();
                try {
                    capStckInIndS = cell.getStringCellValue();
                } catch (java.lang.IllegalStateException ex) {
                    capStckInInd = cell.getNumericCellValue();
                    capStckInIndS = capStckInInd + "";
                }

                formLists(ticker, numStck, priceS, capStckInIndS, stckPK);
                stckPK++;

                if (stckPK == 44){
                    check = true;
                }

            }
        }
    }

    private static void iteratorNext(int i, Iterator<Cell> iterator){
        for (int j = 1; j <= i; j++){
            //System.out.println(iterator.next());
            iterator.next();
        }
    }

    private static void formLists(String ticker, long numStck, String priceS, String capStckInIndexS, int stckPK){
        stocks.add(Stock.builder().stckTicker(ticker).stckNum(numStck).build());
        double price = Double.parseDouble(priceS.replaceAll(",", ".").replaceAll(" ", ""));
        stocksPrices.add(StocksPrices.builder().spStckFK(stckPK).spCurFK(1).spTimeSet(new Timestamp(System.currentTimeMillis())).
                spPrice(price).build());
        double capStckInIndex = Double.parseDouble(capStckInIndexS.replaceAll(",,", ",").
                replaceAll(",", ".").replaceAll(" ", ""));
        long numStckInIndex = (long) (capStckInIndex / price);
        stocksInIndexes.add(StocksInIndexes.builder().siiStckFK(stckPK).siiDoicFK(1).siiNumStck(numStckInIndex).build());
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
