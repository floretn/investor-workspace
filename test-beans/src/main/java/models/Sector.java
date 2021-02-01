package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Sector implements Serializable{
    private int sctrPK;
    private String sctrName;
}
