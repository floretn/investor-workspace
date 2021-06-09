package ru.mephi.iw.ui.beans.auth_pages;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.UserMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.Roles;
import ru.mephi.iw.models.auth.User;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ManagedBean(name="profile")
@ViewScoped
@Data
public class Profile implements Serializable {

    @ManagedProperty(value = "#{auth}")
    private Auth auth;

    private User user;

    private Set<String> rolesOfUserS;

    @PostConstruct
    private void init() {
        user = auth.getCurrentUserInfo().getUser();
        rolesOfUserS = new HashSet<>();
        for (Roles roles : auth.getCurrentUserInfo().getRolesOfUser()) {
            rolesOfUserS.add(roles.getName());
        }
    }

    public void updateUser() {
        if (checkInfo()) {
            try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
                try {
                    sqlSession.getMapper(UserMapper.class).updateUser(user.getId(), user);
                    sqlSession.commit();
                } catch (Exception e) {
                    try {
                        sqlSession.rollback();
                    } catch (Exception e1) {
                        e.addSuppressed(e1);
                        throw e;
                    }
                }
            }
            auth.getCurrentUserInfo().setUser(user);
            auth.setUsername(user.getUsername());
            addMessage(FacesMessage.SEVERITY_INFO,
                    "Успех!", "Изменения приняты!");
        }
    }

    public String deleteUser() {
        new WorkWithCurrentUserInfo().deleteUser(auth.getCurrentUserInfo());
        auth.loginLogout();
        return "/ru/mephi/iw/auth_pages/Auth.xhtml?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        boolean check = true;

        if (!user.getPhone().matches("\\+7[0-9]{10}")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Телефон должен быть представлен в формате +7XXXXXXXXXX");
            check = false;
        }

        if (!user.getEmail().matches(".+@[a-z]+\\.[a-z]+")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Email введён неправильно!");
            check = false;
        }

        return check;
    }
}
