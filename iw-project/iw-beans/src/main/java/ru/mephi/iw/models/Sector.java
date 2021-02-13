package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_sectors*/
@Data
@Builder
public class Sector implements Serializable{
    private int idK;
    private String name;
}
