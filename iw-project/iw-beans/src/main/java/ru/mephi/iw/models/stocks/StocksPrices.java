package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**investor_workspace.t_stocks_prices*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StocksPrices implements Serializable{
    /**sp_pk*/
    private int id;
    /**sp_stck_fk*/
    private int stockId;
    /**sp_cur_fk*/
    private int currencyId;
    /**sp_time_set*/
    private Timestamp settingTime;
    /**sp_price*/
    private double price;
}
