package ru.mephi.iw.ui.beans.hat;

import lombok.Data;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@ManagedBean(name="hat")
@SessionScoped
@Data
public class HatBean implements Serializable {

    /**Иконка "Выйти"*/
    private static final String ICON_TRUE = "fa fa-sign-out";
    /**Иконка "Войти"*/
    private static final String ICON_FALSE = "fa fa-sign-in";
    /**Имя кнопки "Выйти"*/
    private static final String STATUS_TRUE = "Выйти";
    /**Имя кнопки "Войти"*/
    private static final String STATUS_FALSE = "Войти";
    /**Имя пользователя "Гость"*/
    private static final String GUEST = "Гость";

    private String status = STATUS_FALSE;
    private String username = GUEST;
    private boolean check;
    private String icon = ICON_FALSE;

    private CurrentUserInfo currentUserInfo;

    public void setCurrentUserInfo(CurrentUserInfo currentUserInfo) {
        this.currentUserInfo = currentUserInfo;
        username = currentUserInfo.getUser().getUsername();
        status = STATUS_TRUE;
        icon = ICON_TRUE;
        check = true;
    }

    public String loginLogout() {
        if (isCheck()) {
            logout();
        }
        return "/ru/mephi/iw/auth_pages/Auth.xhtml?faces-redirect=true";
    }

    private void logout() {
        username = GUEST;
        check = false;
        status = STATUS_FALSE;
        icon = ICON_FALSE;
        currentUserInfo = null;
        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession()
                .setAttribute("user", null);
    }

    public String goToDownIMOEX() {
        return "/ru/mephi/iw/indexes/IMOEX/DownloadIMOEX.xhtml?faces-redirect=true";
    }

    public String goToProfile() {return "/ru/mephi/iw/auth_pages/Profile.xhtml?faces-redirect=true";}

    public String goToSafetySettings() {return "/ru/mephi/iw/settings/SafetySettings.xhtml?faces-redirect=true";}

    public String goToUsersView() {return "/ru/mephi/iw/users/UsersView.xhtml?faces-redirect=true";}

    public String goToUserBriefcases() {return "/ru/mephi/iw/briefcases/UserBriefcases.xhtml?faces-redirect=true";}
}
