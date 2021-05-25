package ru.mephi.iw.ui.auth_pages;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.AuthInfoMapper;
import ru.mephi.iw.dao.mappers.auth.UserMapper;
import ru.mephi.iw.dao.work.InsertUserInDB;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.User;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="auth")
@SessionScoped
public class Auth implements Serializable {

    private String status = "Войти";
    private String username = "Гость ";
    private String login;
    private String password;
    private boolean check;
    private String icon = "fa fa-sign-in";

    private User user;

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

    public User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
        username = user.getUsername() + " ";
        status = "Выйти";
        check = true;
    }

    public String checkLogin() {
        if (user != null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!", "Сначала нужно выйти из аккаунта!");
            return "";
        }
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            AuthInfo authInfo = sqlSession.getMapper(AuthInfoMapper.class).selectAuthInfo(login);

            if (authInfo == null || !password.equals(authInfo.getPwd())) {
                addMessage(FacesMessage.SEVERITY_ERROR,
                        "Ошибка!", "Неверные логин и/или пароль!");
                return "";
            }

            check = true;
            status = "Выйти";
            icon = "fa fa-sign-out";
            user = sqlSession.getMapper(UserMapper.class).selectUser(authInfo.getUserId());
            username = user.getUsername() + " ";
            return "Profile?faces-redirect=true";
        }
    }

    public String loginLogout() {
        if (isCheck()) {
            logout();
        }
        return "Auth?faces-redirect=true";
    }

    public void logout() {
        login = "";
        password = "";
        username = "Гость ";
        check = false;
        status = "Войти";
        icon = "fa fa-sign-in";
        user = null;
    }

    public String registration() {
        return "Registration?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
