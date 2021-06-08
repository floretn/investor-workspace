package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_roles_of_users*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesOfUsers implements Serializable {
    /**rou_pk*/
    private int id;
    /**rou_usr_fk*/
    private int userId;
    /**rou_rol_fk*/
    private int roleId;
    /**rou_status*/
    private boolean status;
}
