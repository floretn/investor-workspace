package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_currency*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency implements Serializable{
    /**cur_pk*/
    private int id;
    /**cur_name*/
    private String name;
    /**cur_course*/
    private double course;
}
