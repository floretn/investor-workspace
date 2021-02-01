package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Index implements Serializable{
    private int indxPK;
    private String indxName;
}
