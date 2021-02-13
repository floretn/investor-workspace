package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;
import java.sql.Date;

/**investor-workspace.t_date_of_indexes_changes*/
@Data
@Builder
public class DateOfIndexesChanges implements Serializable{
    private int id;
    private int indexId;
    private Date dateOfChange;
}
