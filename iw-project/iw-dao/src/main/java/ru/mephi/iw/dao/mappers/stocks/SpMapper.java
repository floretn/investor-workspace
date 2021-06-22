package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.StocksPrices;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

public interface SpMapper {

    StocksPrices selectPriceForStockByDate(@Param("stockId") int stockId, @Param("date") LocalDate date);

    void insertSp(@Param("sp") StocksPrices sp);

    void deleteSp(@Param("id") int id);
}
