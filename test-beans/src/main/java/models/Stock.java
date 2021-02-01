package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Stock implements Serializable{
    private int stckPK;
    private int stckCmpnFK;
    private String stckName;
    private String stckTicker;
    private long stckNum;
}
