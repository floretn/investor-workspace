package ru.mephi.iw.fill;

import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.models.Stock;
import ru.mephi.iw.models.StocksInIndexes;
import ru.mephi.iw.models.StocksPrices;
import java.io.IOException;
import java.util.List;

class WriteIMOEXClass {

    private static List<Stock> stocksOld = null;
    private static List<StocksPrices> stocksPricesOld = null;
    private static List<StocksInIndexes> stocksInIndexesOld = null;

    WriteIMOEXClass() {
    }

    static void writeInDB(List<Stock> stocks, List<StocksPrices> stocksPrices, List<StocksInIndexes> stocksInIndexes) {

        if (stocksOld == null){
            stocksInIndexesOld = Initial.siiMapper.selectAllSiiIMOEX(Initial.doicMapper.selectDoicIMOEXLast().getId());
            int[] ids = new int[stocksInIndexesOld.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = stocksInIndexesOld.get(i).getStockId();
            }
            stocksOld = Initial.stockMapper.selectAllStocksIMOEX(ids);
            stocksPricesOld = Initial.spMapper.selectAllSpLastIMOEX(ids);
        }

        if (stocksOld.size() >= 45) {
            for (int i = 0; i < 45; i++) {

                if (!stocks.get(i).getTicker().equals(stocksOld.get(i).getTicker())){
                    Initial.stockMapper.updateStock(i + 1, stocks.get(i + 1));
                    Initial.siiMapper.updateSii(i, stocksInIndexes.get(i));
                }

                Initial.spMapper.insertSp(stocksPrices.get(i));

            }
        }else {
            for (int i = 0; i < 45; i++) {
                Initial.stockMapper.insertStock(stocks.get(i));
                stocksPrices.get(i).setStockId(stocks.get(i).getId());
                Initial.spMapper.insertSp(stocksPrices.get(i));
                Initial.siiMapper.insertSii(stocksInIndexes.get(i));
            }
        }
        stocksOld = stocks;
        stocksPricesOld = stocksPrices;
        stocksInIndexesOld = stocksInIndexes;
    }
}
