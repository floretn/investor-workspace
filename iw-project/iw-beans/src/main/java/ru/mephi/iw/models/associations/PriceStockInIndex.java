package ru.mephi.iw.models.associations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksInIndexes;
import ru.mephi.iw.models.stocks.StocksPrices;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**Цена акции в индексе в определённую дату*/
@Data
@Builder
@AllArgsConstructor
public class PriceStockInIndex {

    /**id акции*/
    private int id;
    /**investor-workspace.t_date_of_indexes_changes*/
    private DateOfIndexesChanges dateOfIndexChange;
    /**investor-workspace.t_stocks_in_indexes*/
    private StocksInIndexes stockInIndex;
    /**investor-workspace.t_stocks_prices*/
    private Stock stock;
    /**investor-workspace.t_stocks_prices*/
    private StocksPrices stockPrice;

    public PriceStockInIndex(Integer stckId, Integer idDoic, Integer doicIndexId, Date doicDateChange, Integer siiId,
                             Integer siiStockId, Integer siiDoicId, Long siiNumStock, Integer stockId, Integer stckCompanyId,
                             String stckName, String stckTicker, Integer spId, Integer spStockId, Integer spCurrencyId,
                             Timestamp spTimeSet, BigDecimal spPrice) {
        this.id = stckId;
        dateOfIndexChange = DateOfIndexesChanges.builder().id(idDoic).indexId(doicIndexId).dateOfChange(doicDateChange).build();
        stockInIndex = StocksInIndexes.builder().id(siiId).stockId(siiStockId).dateOfIndexesChangesId(siiDoicId)
                .numberOfStocksInIndex(siiNumStock).build();
        stock = Stock.builder().id(stockId).companyId(stckCompanyId).name(stckName).ticker(stckTicker).build();
        stockPrice = StocksPrices.builder().id(spId).stockId(spStockId).currencyId(spCurrencyId).settingTime(spTimeSet).
                price(spPrice.doubleValue()).build();
    }
}
