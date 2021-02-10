package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class StocksInIndexes implements Serializable{
    private int id;
    private int stockId;
    private int dateOfIndexesChangesId;
    private long numberOfStocksInIndex;
}
