package models;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class StocksPrices implements Serializable{
    private int spPK;
    private int spStckFK;
    private int spCurFK;
    private Timestamp spTimeSet;
    private double spPrice;
}
