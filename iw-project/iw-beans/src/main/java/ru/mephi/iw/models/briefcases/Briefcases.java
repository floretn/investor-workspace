package ru.mephi.iw.models.briefcases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**investor_workspace.t_briefcases*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Briefcases {
    /**brf_pk*/
    private int id;
    /**brf_usr_fk*/
    private int userId;
    /**brf_name*/
    private String name;
    /**brf_desc*/
    private String description;
}
