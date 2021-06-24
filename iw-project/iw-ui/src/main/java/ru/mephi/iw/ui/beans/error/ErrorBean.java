package ru.mephi.iw.ui.beans.error;

import lombok.Data;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@ManagedBean(name = "error")
@ViewScoped
@Data
public class ErrorBean implements Serializable {

    private String msg = "";

    @PostConstruct
    private void init() {
        String gotMsg = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter("msg");
        if (gotMsg != null) {
            msg += gotMsg;
        }
    }
}
