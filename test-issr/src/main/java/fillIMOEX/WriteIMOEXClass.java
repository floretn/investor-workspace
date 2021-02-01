package fillIMOEX;


import dao.iw.methods.SiiMethods;
import dao.iw.methods.SpMethods;
import dao.iw.methods.StockMethods;
import models.Stock;
import models.StocksInIndexes;
import models.StocksPrices;
import java.io.IOException;
import java.util.List;

class WriteIMOEXClass {

    private static List<Stock> stocksOld = null;
    private static List<StocksPrices> stocksPricesOld = null;
    private static List<StocksInIndexes> stocksInIndexesOld = null;

    WriteIMOEXClass() {
    }

    static void writeInDB(List<Stock> stocks, List<StocksPrices> stocksPrices, List<StocksInIndexes> stocksInIndexes)
            throws IOException {
        StockMethods stockMethods = new StockMethods();
        SpMethods spMethods = new SpMethods();
        SiiMethods siiMethods = new SiiMethods();

        if (stocksOld == null){
            stocksOld = stockMethods.showAllStocks();
            stocksPricesOld = spMethods.showAllSp();
            stocksInIndexesOld = siiMethods.showAllSii();
        }

        if (stocksOld.size() >= 45) {
            for (int i = 0; i < 45; i++) {
                if (!stocks.get(i).equals(stocksOld.get(i))){
                    stockMethods.updateStock(i, stocks.get(i));
                }

                spMethods.insertSp(stocksPrices.get(i));

                if (!stocksInIndexes.get(i).equals(stocksInIndexesOld.get(i))){
                    siiMethods.updateSii(i, stocksInIndexes.get(i));
                }
            }
        }else {
            for (int i = 0; i < 45; i++) {
                stockMethods.insertStock(stocks.get(i));
                spMethods.insertSp(stocksPrices.get(i));
                siiMethods.insertSii(stocksInIndexes.get(i));
            }
        }
        stocksOld = stocks;
        stocksPricesOld = stocksPrices;
        stocksInIndexesOld = stocksInIndexes;
    }
}
