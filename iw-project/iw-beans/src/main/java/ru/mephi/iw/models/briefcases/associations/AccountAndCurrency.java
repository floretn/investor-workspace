package ru.mephi.iw.models.briefcases.associations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.Currency;
import ru.mephi.iw.models.briefcases.Account;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountAndCurrency {
    /**id счёта*/
    private int id;

    private Account account;
    private Currency currency;

    @Override
    public String toString() {
        return account.getAmount() + " " + currency.getName();
    }
}
