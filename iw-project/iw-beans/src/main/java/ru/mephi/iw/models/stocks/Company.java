package ru.mephi.iw.models.stocks;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Company implements Serializable{
    private int id;
    private String name;
}
