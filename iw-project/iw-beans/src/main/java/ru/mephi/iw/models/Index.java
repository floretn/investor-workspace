package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class Index implements Serializable{
    private int id;
    private String name;
}
