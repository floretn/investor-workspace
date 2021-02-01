package models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Company implements Serializable{
    private int cmpnPK;
    private String cmpnName;
}
