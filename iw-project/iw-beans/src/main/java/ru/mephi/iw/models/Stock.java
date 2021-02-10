package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Stock implements Serializable{
    private int id;
    private int companyId;
    private String name;
    private String ticker;
}
