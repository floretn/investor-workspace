package ru.mephi.iw.ui.beans.briefcases;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.briefcases.BriefcasesMapper;
import ru.mephi.iw.dao.work.WorkWithBriefcases;
import ru.mephi.iw.models.briefcases.Briefcases;
import ru.mephi.iw.ui.beans.hat.Hat;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ManagedBean(name = "ub")
@ViewScoped
@Data
public class UserBriefcases implements Serializable {

    private List<Briefcases> briefcases;
    private List<Briefcases> briefcasesShow;

    private String nameSearch = "";
    private String descSearch = "";

    private Briefcases briefcaseForDelete;

    @ManagedProperty(value = "#{hat}")
    private Hat hat;

    @PostConstruct
    private void init() {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            briefcases = sqlSession.getMapper(BriefcasesMapper.class).
                    selectBriefcasesOfUser(hat.getCurrentUserInfo().getId());
        }
        briefcasesShow = briefcases;
    }

    public void updateBriefcases() {
        briefcasesShow = briefcases.parallelStream().filter
                (b -> (nameSearch.equals("") || b.getName().toLowerCase(Locale.ROOT)
                        .contains(nameSearch.toLowerCase(Locale.ROOT)))
                        && (descSearch.equals("") || b.getDescription().toLowerCase(Locale.ROOT)
                        .contains(descSearch.toLowerCase(Locale.ROOT))))
                .collect(Collectors.toList());

        if (briefcasesShow.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение!", "Совпадений не найдено!");
        }
    }

    public void trash() {
        briefcasesShow = briefcases;
        nameSearch = "";
        descSearch = "";
    }

    public void prepareForDelete(Briefcases briefcase) {
        briefcaseForDelete = briefcase;
    }

    public void deleteBriefcase() {
        try {
            new WorkWithBriefcases().deleteBriefcase(briefcaseForDelete);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Не удалось удалить портфель!\nОбратитесь к админимтратору");
            e.printStackTrace();
            return;
        }
        briefcases.remove(briefcaseForDelete);
        briefcasesShow = briefcases;
        briefcaseForDelete = null;
        addMessage(FacesMessage.SEVERITY_INFO,
                "Информация!", "Портфель успешно удалён!");
    }

    public void cancelDelete() {
        briefcaseForDelete = null;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String viewBriefcase(Briefcases briefcase) {
        return "/ru/mephi/iw/briefcases/BriefcaseView.xhtml?id="+ briefcase.getId() + "faces-redirect=true";
    }

    public String goToAddBriefcase() {return "/ru/mephi/iw/briefcases/AddBriefcase.xhtml?faces-redirect=true";}

}
