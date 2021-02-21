package ru.mephi.iw.bean;

import ru.mephi.iw.models.Stock;
import ru.mephi.iw.models.StocksInIndexes;
import ru.mephi.iw.models.StocksPrices;
import lombok.*;

@Data
@AllArgsConstructor
public class Helper {
    Stock stock;
    StocksPrices sp;
    StocksInIndexes sii;

    public Helper(){}
}
