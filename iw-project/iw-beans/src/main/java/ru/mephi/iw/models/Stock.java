package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_stocks*/
@Data
@Builder
public class Stock implements Serializable{
    private int id;
    private Integer companyId;
    private String name;
    private String ticker;
}
