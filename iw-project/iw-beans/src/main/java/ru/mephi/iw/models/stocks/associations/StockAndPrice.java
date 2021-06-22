package ru.mephi.iw.models.stocks.associations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.Currency;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksPrices;

/**Цена акции*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAndPrice {
    /**id акции*/
    private int id;

    private Stock stock;
    private StocksPrices stockPrice;
    private Currency currencyOfPrice;
}
