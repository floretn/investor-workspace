package ru.mephi.iw.models;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_sectors_of_companies*/
@Data
@Builder
public class SectorsOfCompanies implements Serializable{
    /**soc_pk*/
    private int id;
    /**soc_cmpn_fk*/
    private int companyId;
    /**soc_sctr_fk*/
    private int sectorId;
}
