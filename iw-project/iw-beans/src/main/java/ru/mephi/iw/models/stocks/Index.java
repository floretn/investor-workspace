package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_indexes*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Index implements Serializable{
    /**indx_pk*/
    private int id;
    /**indx_name*/
    private String name;
}
