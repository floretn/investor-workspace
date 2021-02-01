package models;

import lombok.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
public class DateOfIndexesChanges implements Serializable{
    private int doicPK;
    private int doicIndxFK;
    private Date doicDateChng;
}
