package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_currency*/
@Data
@Builder
public class Currency implements Serializable{
    /**cur_pk*/
    private int id;
    /**cur_name*/
    private String name;
}
