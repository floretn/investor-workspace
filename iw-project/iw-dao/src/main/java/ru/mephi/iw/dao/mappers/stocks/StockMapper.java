package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StockMapper {

    Stock selectStockByTicker(@Param("ticker") String ticker);

    void insertStock(@Param("stock") Stock stock);
}
