package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor-workspace.t_sectors_of_companies*/
@Data
@Builder
public class SectorsOfCompanies implements Serializable{
    private int id;
    private int companyId;
    private int sectorId;
}
