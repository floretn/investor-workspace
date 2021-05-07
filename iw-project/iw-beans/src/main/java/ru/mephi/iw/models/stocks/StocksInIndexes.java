package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_stocks_in_indexes*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StocksInIndexes implements Serializable{
    /**sii_pk*/
    private int id;
    /**sii_stck_fk*/
    private int stockId;
    /**sii_doic_fk*/
    private int dateOfIndexesChangesId;
    /**sii_num_stck*/
    private long numberOfStocksInIndex;
}
