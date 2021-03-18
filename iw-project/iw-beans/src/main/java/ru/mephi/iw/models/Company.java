package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_companies*/
@Data
@Builder
public class Company implements Serializable{
    /**cmpn_pk*/
    private int id;
    /**cmpn_name*/
    private String name;
}
