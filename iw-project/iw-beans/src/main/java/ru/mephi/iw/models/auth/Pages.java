package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_pages*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pages implements Serializable {
    /**pg_pk*/
    private int id;
    /**pg_path*/
    private String path;
}
