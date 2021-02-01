package bean;

import models.Stock;
import models.StocksInIndexes;
import models.StocksPrices;

import java.io.IOException;

public class Helper {
    Stock stock;
    StocksPrices sp;
    StocksInIndexes sii;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StocksPrices getSp() {
        return sp;
    }

    public void setSp(StocksPrices sp) {
        this.sp = sp;
    }

    public StocksInIndexes getSii() {
        return sii;
    }

    public void setSii(StocksInIndexes sii) {
        this.sii = sii;
    }

}
