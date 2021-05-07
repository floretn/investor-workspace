package ru.mephi.iw.models.stocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**investor_workspace.t_index_was_upload*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexWasUpload {
    /**iwu_pk*/
    private int id;
    /**iwu_indx_fk*/
    private int indexId;
    /**iwu_date*/
    private LocalDate date;
    /**iwu_check*/
    private boolean check;
}
