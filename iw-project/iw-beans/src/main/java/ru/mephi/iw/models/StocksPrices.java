package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class StocksPrices implements Serializable{
    private int id;
    private int stockId;
    private int currencyId;
    private Timestamp settingTime;
    private double price;
}
