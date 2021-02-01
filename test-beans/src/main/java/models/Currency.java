package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Currency implements Serializable{
    private int curPK;
    private String curName;
}
