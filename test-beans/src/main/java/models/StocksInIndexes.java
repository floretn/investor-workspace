package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class StocksInIndexes implements Serializable{
    private int siiPK;
    private int siiStckFK;
    private int siiDoicFK;
    private long siiNumStck;
}
