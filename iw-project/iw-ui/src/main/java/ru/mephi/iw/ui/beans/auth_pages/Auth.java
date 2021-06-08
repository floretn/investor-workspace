package ru.mephi.iw.ui.beans.auth_pages;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.security.PwdCoder;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServlet;
import java.io.Serializable;

@ManagedBean(name="auth")
@SessionScoped
public class Auth extends HttpServlet implements Serializable {

    private String status = "Войти";
    private String username = "Гость";
    private String login;
    private String password;
    private boolean check;
    private String icon = "fa fa-sign-in";
    private final PwdCoder pwdCoder = new PwdCoder();

    private CurrentUserInfo currentUserInfo;

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCheck() {
        return check;
    }

    public String getIcon() {
        return icon;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public CurrentUserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }

    void setUserInfo(CurrentUserInfo currentUserInfo) {
        this.currentUserInfo = currentUserInfo;
        username = currentUserInfo.getUser().getUsername() + " ";
        status = "Выйти";
        check = true;
    }

    public String checkLogin() {

        if (currentUserInfo != null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!", "Сначала нужно выйти из аккаунта!");
            return "";
        }

        password = pwdCoder.encodePwd(password);
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            currentUserInfo = sqlSession.getMapper(CurrentUserInfoMapper.class).selectCurrentUserInfo(login,
                    password);
        }

        if (currentUserInfo == null) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Неверные логин и/или пароль!");
            return "";
        }

        check = true;
        status = "Выйти";
        icon = "fa fa-sign-out";
        username = currentUserInfo.getUser().getUsername();
        login = "";
        password = "";
        return "/ru/mephi/iw/auth_pages/Profile.xhtml?faces-redirect=true";
    }

    public String loginLogout() {
        if (isCheck()) {
            logout();
        }
        return "/ru/mephi/iw/auth_pages/Auth.xhtml?faces-redirect=true";
    }

    private void logout() {
        login = "";
        password = "";
        username = "Гость";
        check = false;
        status = "Войти";
        icon = "fa fa-sign-in";
        currentUserInfo = null;
    }

    public String registration() {
        return "/ru/mephi/iw/auth_pages/Registration.xhtml?faces-redirect=true";
    }

    public String goToDownIMOEX() {
        return "/ru/mephi/iw/IMOEX/DownloadIMOEX.xhtml?faces-redirect=true";
    }

    public String goToProfile() {return "/ru/mephi/iw/auth_pages/Profile.xhtml?faces-redirect=true";}

    public String goToSafetySettings() {return "/ru/mephi/iw/settings/SafetySettings.xhtml?faces-redirect=true";}

    public String goToUsersView() {return "/ru/mephi/iw/views/UsersView.xhtml?faces-redirect=true";}

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
