package ru.mephi.iw.models.stocks.associations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksInIndexes;
import ru.mephi.iw.models.stocks.StocksPrices;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

/**Цена акции в индексе в определённую дату*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceStockInIndex {

    /**id акции*/
    private int id;
    /**investor_workspace.t_date_of_indexes_changes*/
    private DateOfIndexesChanges dateOfIndexChange;
    /**investor_workspace.t_stocks_in_indexes*/
    private StocksInIndexes stockInIndex;
    /**investor_workspace.t_stocks_prices*/
    private Stock stock;
    /**investor_workspace.t_stocks_prices*/
    private StocksPrices stockPrice;

}
