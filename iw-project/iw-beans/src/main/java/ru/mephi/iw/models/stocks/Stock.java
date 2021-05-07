package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_stocks*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock implements Serializable{
    /**stck_pk*/
    private int id;
    //TODO сделать примитивом
    /**stck_cmpn_fk*/
    private Integer companyId;
    /**stck_name*/
    private String name;
    /**stck_ticker*/
    private String ticker;
}
