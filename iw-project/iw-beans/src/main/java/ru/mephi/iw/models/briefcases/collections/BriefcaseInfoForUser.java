package ru.mephi.iw.models.briefcases.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.briefcases.Account;
import ru.mephi.iw.models.briefcases.BriefcaseStates;
import ru.mephi.iw.models.briefcases.Briefcases;
import ru.mephi.iw.models.briefcases.StocksInBriefcases;
import ru.mephi.iw.models.stocks.associations.StockAndPrice;

import java.util.Set;

/**Вся информация о портфеле для пользователя*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BriefcaseInfoForUser {
    /**id портфеля*/
    private int id;

    private Briefcases briefcase;
    private BriefcaseStates briefcaseState;

    private Set<Account> accounts;
    private Set<StocksInBriefcases> stocksInBriefcase;
    private Set<StockAndPrice> stockPrices;

}
