package ru.mephi.iw.models.auth.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.iw.models.auth.*;

import java.util.List;
import java.util.Set;

/**Вся информация о пользователе*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersInfoForAdmin {

    /**id юзера*/
    private int id;

    private User user;

    private Set<AuthInfo> authInfo;
    private Set<RolesOfUsers> rolesOfUser;
}
