package ru.mephi.iw.ui.beans.views;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.constants.RolesKeys;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.collections.UsersInfoForAdminMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.RolesOfUsers;
import ru.mephi.iw.models.auth.collections.UsersInfoForAdmin;
import ru.mephi.iw.security.PwdCoder;
import ru.mephi.iw.ui.beans.auth_pages.Auth;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean(name="usersView")
@SessionScoped
@Data
public class UsersView implements Serializable {

    private List<UsersInfoForAdmin> usersInfo;
    private List<UsersInfoForAdmin> usersInfoInDB;

    private String username = "";
    private String lastName = "";
    private String firstName = "";
    private String patronymic = "";
    private String phone = "";
    private String email = "";
    private int role = -1;

    private final int indexAdmin = RolesKeys.ADMIN_KEY;
    private final int indexUser = RolesKeys.USER_KEY;

    private UsersInfoForAdmin selectedUserForUpdate = null;
    private UsersInfoForAdmin selectedUserForDelete;

    private String pwd;
    private String pwd1;
    private String pwdOld;

    private PwdCoder pwdCoder = new PwdCoder();

    @ManagedProperty(value = "#{auth}")
    private Auth auth;

    @PostConstruct
    private void init() {
        updateData();
    }

    public String prepareForUpdate(UsersInfoForAdmin currentUserInfo) {
        selectedUserForUpdate = currentUserInfo;
        return "/ru/mephi/iw/views/UserInfoChange.xhtml?faces-redirect=true";
    }

    public void updateData() {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            usersInfoInDB = sqlSession.getMapper(UsersInfoForAdminMapper.class).selectUserInfo();
            usersInfo = usersInfoInDB;
        }
    }

    public void prepareForDelete(UsersInfoForAdmin currentUserInfo) {
        selectedUserForDelete = currentUserInfo;
    }

    public void updateUserList() {
        usersInfo = usersInfoInDB.stream().filter(u -> username.equals("") || u.getUser().getUsername().equals(username))
                .filter(u -> lastName.equals("") || u.getUser().getLastName().equals(lastName))
                .filter(u -> firstName.equals("") || u.getUser().getFirstName().equals(firstName))
                .filter(u -> patronymic.equals("") || u.getUser().getPatronymic().equals(patronymic))
                .filter(u -> phone.equals("") || u.getUser().getPhone().equals(phone))
                .filter(u -> email.equals("") || u.getUser().getEmail().equals(email))
                .filter(u -> role == -1 || checkRole(u.getRolesOfUser(), role) ||
                        (role == 0 && u.getRolesOfUser().isEmpty()))
                .collect(Collectors.toList());
        if (usersInfo.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение!", "Совпадений не найдено!");
        }
    }

    private boolean checkRole(Set<RolesOfUsers> rolesOfUsers, int roleId) {
        for (RolesOfUsers roleOfUser : rolesOfUsers) {
            if (roleOfUser.getRoleId() == roleId && roleOfUser.isStatus()) {
                return true;
            }
        }
        return false;
    }

    public void deleteUser() {

        if (checkInfo()) {
            return;
        }

        new WorkWithCurrentUserInfo().deleteUser(selectedUserForDelete);
        usersInfoInDB.remove(selectedUserForDelete);
        usersInfo.remove(selectedUserForDelete);
        selectedUserForDelete = null;
    }

    public void trash() {
        username = "";
        lastName = "";
        firstName = "";
        patronymic = "";
        phone = "";
        email = "";
        usersInfo = usersInfoInDB;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        if (!pwdCoder.encodePwd(pwdOld).equals(auth.getCurrentUserInfo().getAuthInfo().getPwd())) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Неверный пароль!");
            return true;
        }

        return false;
    }
}
