package ru.mephi.iw.ui.auth_pages;

import lombok.Data;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="profile")
@SessionScoped
@Data
public class Profile implements Serializable {

    @ManagedProperty(value = "#{auth}")
    Auth auth;

    public String goDownloadIMOEX() {
        return "ru/mephi/iw/IMOEX/DownloadIMOEX?faces-redirect=true";
    }
}
