package ru.mephi.iw.models.briefcases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**investor_workspace.t_stocks_in_briefcases*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StocksInBriefcases {
    /**sib_pk*/
    private int id;
    /**sib_bs_fk*/
    private int briefcaseStateId;
    /**sib_stck_fk*/
    private int stockId;
    /**sib_stck_num*/
    private long stocksNumber;
    /**sib_type*/
    private String type;
}
