package ru.mephi.iw.dao.mappers.auth.collections;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import java.util.Set;

public interface CurrentUserInfoMapper {
    CurrentUserInfo selectCurrentUserInfo(@Param("login") String login, @Param("pwd") String pwd);

    /**
     * Собирается в Set потому что сначала идёт выборка всех пользователей из базы без учёта ролей,
     * а потом выбор пользователей с ролями. Таким образом не отбрасываются юзеры, не имеющие роли (см. Маппер).
     * А так как хеш CurrentUserInfo считается только с учётом поля user и id, при повторной выборке пользователей
     * те, кто был найден повторно (то есть имеет хотя бы одну роль) будет заменён в Set-е.
     */
    Set<CurrentUserInfo> selectAllCurrentUserInfo();
}
