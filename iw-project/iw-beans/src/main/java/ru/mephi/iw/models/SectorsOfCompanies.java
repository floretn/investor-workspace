package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
public class SectorsOfCompanies implements Serializable{
    private int id;
    private int companyId;
    private int sectorId;
}
