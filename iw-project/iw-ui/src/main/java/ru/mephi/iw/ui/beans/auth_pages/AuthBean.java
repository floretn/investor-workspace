package ru.mephi.iw.ui.beans.auth_pages;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.AuthInfoMapper;
import ru.mephi.iw.dao.mappers.auth.UserMapper;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.User;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.security.PwdCoder;
import ru.mephi.iw.ui.beans.hat.HatBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashSet;

@ManagedBean(name="auth")
@ViewScoped
@Data
public class AuthBean implements Serializable {

    private String login;
    private String password;
    private CurrentUserInfo currentUserInfo;

    private final PwdCoder pwdCoder = new PwdCoder();

    @ManagedProperty(value = "#{hat}")
    private HatBean hat;

    public String checkLogin() {

        if (hat.getCurrentUserInfo() != null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!", "Сначала нужно выйти из аккаунта!");
            return "";
        }

        password = pwdCoder.encodePwd(password);
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            currentUserInfo = sqlSession.getMapper(CurrentUserInfoMapper.class).selectCurrentUserInfo(login,
                    password);

            if (currentUserInfo == null) {
                AuthInfo authInfo = sqlSession.getMapper(AuthInfoMapper.class).selectAuthInfoByLogAndPwd(login, password);
                if (authInfo == null) {
                    addMessage(FacesMessage.SEVERITY_ERROR,
                            "Ошибка!", "Неверные логин и/или пароль!");
                    return "";
                }
                User user = sqlSession.getMapper(UserMapper.class).selectUser(authInfo.getUserId());
                currentUserInfo = new CurrentUserInfo(user.getId(), authInfo, user,
                        new HashSet<>(), new HashSet<>());
            }
        }

        hat.setCurrentUserInfo(currentUserInfo);
        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession()
                .setAttribute("user", currentUserInfo);
        return "/ru/mephi/iw/auth_pages/Profile.xhtml?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String registration() {
        return "/ru/mephi/iw/auth_pages/Registration.xhtml?faces-redirect=true";
    }

}
