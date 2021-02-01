package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class SectorsOfCompanies implements Serializable{
    private int socPK;
    private int socCmpnFK;
    private int socSctrFK;
}
