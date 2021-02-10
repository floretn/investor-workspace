package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
public class DateOfIndexesChanges implements Serializable{
    private int id;
    private int indexId;
    private Date dateOfChange;
}
