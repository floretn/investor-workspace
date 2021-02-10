package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Sector implements Serializable{
    private int idK;
    private String name;
}
