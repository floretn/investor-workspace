package ru.mephi.iw.models.briefcases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**investor_workspace.t_account*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    /**acc_pk*/
    private int id;
    /**acc_bs_fk*/
    private int bsId;
    /**acc_cur_fk*/
    private int currencyId;
    /**acc_am*/
    private double amount;
}
