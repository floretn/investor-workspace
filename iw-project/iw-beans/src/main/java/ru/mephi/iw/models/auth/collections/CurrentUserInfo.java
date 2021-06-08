package ru.mephi.iw.models.auth.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.Pages;
import ru.mephi.iw.models.auth.Roles;
import ru.mephi.iw.models.auth.User;

import java.util.List;
import java.util.Set;

/**Вся информация о текущем пользователе*/
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
}
