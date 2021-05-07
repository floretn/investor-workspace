package ru.mephi.iw.dao.mappers.stocks;

import ru.mephi.iw.models.stocks.StocksInIndexes;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SiiMapper {

    void insertSii(@Param("sii") StocksInIndexes sii);
}
