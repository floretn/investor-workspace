package ru.mephi.iw.dao.mappers.briefcases;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.briefcases.Account;
import java.util.List;

public interface AccountMapper {
    List<Account> selectAccountOfBS(@Param("bsId") int bsId);

    void insertAccount(@Param("account") Account account);

    void deleteAllAccountsInBS(@Param("bsId") int bsId);
}
