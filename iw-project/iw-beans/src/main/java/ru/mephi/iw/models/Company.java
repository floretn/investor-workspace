package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_companies*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable{
    /**cmpn_pk*/
    private int id;
    /**cmpn_name*/
    private String name;
}
