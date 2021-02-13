package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_currency*/
@Data
@Builder
public class Currency implements Serializable{
    private int id;
    private String name;
}
