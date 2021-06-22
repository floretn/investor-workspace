package ru.mephi.iw.models.auth.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.Pages;
import ru.mephi.iw.models.auth.Roles;
import ru.mephi.iw.models.auth.User;

import java.util.Objects;
import java.util.Set;

/**Вся информация о текущей сессии пользователя*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserInfo {

    /**id юзера*/
    private int id;

    private AuthInfo authInfo;
    private User user;

    private Set<Roles> rolesOfUser;
    private Set<Pages> pagesOfUser;

    /**Если пользователи идентичны, значит и вся остальная информация идентична.*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrentUserInfo)) return false;
        CurrentUserInfo that = (CurrentUserInfo) o;
        return getId() == that.getId() && getUser().equals(that.getUser());
    }

    /**Если пользователи идентичны, значит и вся остальная информация идентична.*/
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser());
    }
}
