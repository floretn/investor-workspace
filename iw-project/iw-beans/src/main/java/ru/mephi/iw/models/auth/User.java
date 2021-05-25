package ru.mephi.iw.models.auth;

import lombok.*;
import java.io.Serializable;

/**investor_workspace.t_users*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**usr_pk*/
    private int id;
    /**usr_lnm*/
    private String lastName;
    /**usr_fnm*/
    private String firstName;
    /**usr_ptrnm*/
    private String patronymic;
    /**usr_phn*/
    private String phone;
    /**usr_email*/
    private String email;
    /**usr_un*/
    private String username;
}
