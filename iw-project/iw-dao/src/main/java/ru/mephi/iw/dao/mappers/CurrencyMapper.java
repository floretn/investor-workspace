package ru.mephi.iw.dao.mappers;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.Currency;

public interface CurrencyMapper {

    Currency selectCurrencyByName(@Param("name") String name);

    Currency selectCurrencyById(@Param("id") int id);

    void insertCurrency(@Param("currency") Currency currency);
}
