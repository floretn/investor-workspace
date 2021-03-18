package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;
import java.sql.Date;

/**investor-workspace.t_date_of_indexes_changes*/
@Data
@Builder
@AllArgsConstructor
public class DateOfIndexesChanges implements Serializable{
    /**doic_pk*/
    private int id;
    /**doic_indx_fk*/
    private int indexId;
    /**doic_date_chng*/
    private Date dateOfChange;
}
