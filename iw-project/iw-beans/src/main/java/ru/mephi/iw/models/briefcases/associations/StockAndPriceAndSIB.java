package ru.mephi.iw.models.briefcases.associations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.Currency;
import ru.mephi.iw.models.briefcases.StocksInBriefcases;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksPrices;

/**Сопостовление акции в портфеле и цены акции*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAndPriceAndSIB {
    /**id акции в портфеле (stockInBriefcase)*/
    private int id;

    private StocksInBriefcases stockInBriefcase;
    private Stock stock;
    private StocksPrices stockPrice;
    private Currency currencyOfPrice;
}
