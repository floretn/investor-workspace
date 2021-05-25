package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_sectors*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sector implements Serializable{
    /**sctr_pk*/
    private int idK;
    /**sctr_name*/
    private String name;
}
