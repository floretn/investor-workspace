package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_companies*/
@Data
@Builder
public class Company implements Serializable{
    private int id;
    private String name;
}
