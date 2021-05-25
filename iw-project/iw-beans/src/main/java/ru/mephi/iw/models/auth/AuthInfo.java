package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;

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
}
