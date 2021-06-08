package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_roles_of_users*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagesForRoles implements Serializable {
    /**rfr_pk*/
    private int id;
    /**rfr_pg_fk*/
    private int pageId;
    /**rfr_rol_fk*/
    private int roleId;
}
