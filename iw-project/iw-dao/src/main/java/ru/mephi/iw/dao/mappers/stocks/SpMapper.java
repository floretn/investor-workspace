package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.StocksPrices;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SpMapper {

    void insertSp(@Param("sp") StocksPrices sp);
}
