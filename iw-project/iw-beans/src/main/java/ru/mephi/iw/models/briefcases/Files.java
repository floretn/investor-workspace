package ru.mephi.iw.models.briefcases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**investor_workspace.t_files*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Files {
    /**fl_pk*/
    private int id;
    /**fl_name*/
    private String name;
    /**fl_date_dwn*/
    private LocalDate dateDownload;
    /**fl_content*/
    private byte[] content;
}
