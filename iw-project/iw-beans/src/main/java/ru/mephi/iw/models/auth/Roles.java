package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_roles*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements Serializable {
    /**rol_pk*/
    private int id;
    /**rol_name*/
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
