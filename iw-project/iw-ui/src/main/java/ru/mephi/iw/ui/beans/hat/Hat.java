package ru.mephi.iw.ui.beans.hat;

import lombok.Data;
import ru.mephi.iw.ui.beans.auth_pages.Auth;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="hat")
@SessionScoped
@Data
public class Hat {

    /**Иконка "Выйти"*/
    private static final String ICON_TRUE = "fa fa-sign-out";
    /**Иконка "Войти"*/
    private static final String ICON_FALSE = "fa fa-sign-in";
    /**Имя кнопки "Выйти"*/
    private static final String STATUS_TRUE = "Выйти";
    /**Имя кнопки "Войти"*/
    private static final String STATUS_FALSE = "Войти";
    /**Имя пользователя "Гость"*/
    public static final String GUEST = "Гость ";

    @ManagedProperty(value = "#{auth}")
    Auth auth;

    private String status = STATUS_FALSE;
    private String username = GUEST;
    private String icon = ICON_FALSE;

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getIcon() {
        return icon;
    }

    public void setInfo(String username, boolean check){
        if (check) {
            status = STATUS_TRUE;
            icon = ICON_TRUE;
            this.username = username;
        } else {
            status = STATUS_FALSE;
            icon = ICON_FALSE;
            this.username = GUEST;
        }
    }

    public String loginLogout() {
        if (auth != null) {
            return auth.loginLogout();
        }
        return "";
    }

    public String goToDownIMOEX() {
        return "/ru/mephi/iw/IMOEX/DownloadIMOEX.xhtml?faces-redirect=true";
    }

    public String goToProfile() {return "/ru/mephi/iw/auth_pages/Profile.xhtml?faces-redirect=true";}
}
