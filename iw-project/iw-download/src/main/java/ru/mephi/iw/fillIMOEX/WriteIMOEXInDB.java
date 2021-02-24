package ru.mephi.iw.fillIMOEX;

import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.models.DateOfIndexesChanges;
import ru.mephi.iw.models.Stock;
import ru.mephi.iw.models.StocksInIndexes;
import ru.mephi.iw.models.StocksPrices;

import java.sql.Date;
import java.util.List;

class WriteIMOEXInDB {

    private static List<Stock> stocksOld = null;
    private static List<StocksInIndexes> stocksInIndexesOld = null;

    private static final List<Stock> stocks = ReadIMOEXFromFile.stocks;
    private static final List<StocksPrices> stocksPrices = ReadIMOEXFromFile.stocksPrices;
    private static final List<StocksInIndexes> stocksInIndexes = ReadIMOEXFromFile.stocksInIndexes;

    WriteIMOEXInDB() {
    }

    static void writeInDB() {

        if (stocksOld == null){
            stocksInIndexesOld = Initial.siiMapper.selectAllSiiIMOEX(Initial.doicMapper.selectDoicIMOEXLast().getId());
            int[] ids = new int[stocksInIndexesOld.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = stocksInIndexesOld.get(i).getStockId();
            }
            stocksOld = Initial.stockMapper.selectAllStocksIMOEX(ids);
        }

        if (stocksOld.size() != 0) {
            boolean check = true;
            boolean checkDop = true;
            for (int i = 0; i < 45; i++) {

                for (int j = 0; j <= 45; j++) {
                    if (stocks.get(i).getTicker().equals(stocksOld.get(j).getTicker())) {
                        stocks.get(i).setId(stocksOld.get(j).getId());
                        check = true;
                        break;
                    }else {
                        check = false;
                    }
                }

                if (!check) {
                    checkDop = false;
                }
            }

            if (checkDop) {
                for (int i = 0; i < 45; i++) {
                    Initial.stockMapper.updateStock(stocks.get(i).getId(), stocks.get(i));
                    stocksPrices.get(i).setStockId(stocks.get(i).getId());
                    Initial.spMapper.insertSp(stocksPrices.get(i));
                }
            } else {
                ifIMOEXWasChanged();
            }
        }else {
            for (int i = 0; i < 45; i++) {
                Initial.stockMapper.insertStock(stocks.get(i));
                stocksPrices.get(i).setStockId(stocks.get(i).getId());
                Initial.spMapper.insertSp(stocksPrices.get(i));
                stocksInIndexes.get(i).setStockId(stocks.get(i).getId());
                Initial.siiMapper.insertSii(stocksInIndexes.get(i));
            }
        }
        stocksOld = stocks;
    }

    private static void ifIMOEXWasChanged () {
        DateOfIndexesChanges doic = DateOfIndexesChanges.builder().indexId(1).
                dateOfChange(new Date((new java.util.Date()).getTime())).build();
        Initial.doicMapper.insertDoic(doic);

        Stock stock;
        for (int i = 0; i < 45; i++) {
            stock = Initial.stockMapper.selectStockByTicker(stocks.get(i).getTicker());
            if (stock == null) {
                Initial.stockMapper.insertStock(stocks.get(i));
            } else {
                stocks.get(i).setId(stock.getId());
                Initial.stockMapper.updateStock(stock.getId(), stocks.get(i));
            }

            stocksInIndexes.get(i).setStockId(stocks.get(i).getId());
            stocksInIndexes.get(i).setDateOfIndexesChangesId(doic.getId());
            stocksInIndexes.get(i).setStockId(stocksInIndexesOld.get(i).getStockId());
            Initial.siiMapper.updateSii(stocksInIndexes.get(i).getId(), stocksInIndexes.get(i));

            stocksPrices.get(i).setStockId(stocks.get(i).getId());
            Initial.spMapper.insertSp(stocksPrices.get(i));
        }

        stocksOld = stocks;
        stocksInIndexesOld = stocksInIndexes;
    }
}
