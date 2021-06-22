package ru.mephi.iw.models.briefcases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**investor_workspace.t_briefcases_sates*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BriefcaseStates {
    /**bs_pk*/
    private int id;
    /**bs_brf_fk*/
    private int briefcaseId;
    /**brf_fl_fk*/
    private int fileId;
    /**brf_date*/
    private LocalDate date;
}
