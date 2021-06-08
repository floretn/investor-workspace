package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

/**investor_workspace.t_auth_info*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfo implements Serializable {
    /**ai_pk*/
    private int id;
    /**ai_usr_fk*/
    private int userId;
    /**ai_login*/
    private String login;
    /**ai_pwd*/
    private String pwd;

    public AuthInfo(String login, String pwd) {
        this.login = login;
        this.pwd = pwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthInfo)) return false;
        AuthInfo authInfo = (AuthInfo) o;
        return getLogin().equals(authInfo.getLogin()) && getPwd().equals(authInfo.getPwd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getPwd());
    }
}
