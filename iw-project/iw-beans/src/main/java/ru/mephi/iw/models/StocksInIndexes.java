package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_stocks_in_indexes*/
@Data
@Builder
public class StocksInIndexes implements Serializable{
    private int id;
    private int stockId;
    private int dateOfIndexesChangesId;
    private long numberOfStocksInIndex;
}
