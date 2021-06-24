package ru.mephi.iw.ui.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebFilter
public class ExistsPageFilter implements Filter {

    private static final List<String> EXISTS_PAGES = new ArrayList<>(Arrays.asList("/", "/ru/mephi/iw/auth_pages/Auth.xhtml"
            , "/ru/mephi/iw/auth_pages/Profile.xhtml", "/ru/mephi/iw/briefcases/UserBriefcases.xhtml"
            , "/ru/mephi/iw/briefcases/Donut.xhtml", "/ru/mephi/iw/briefcases/BriefcaseView.xhtml"
            , "/ru/mephi/iw/briefcases/AddBriefcase.xhtml", "/ru/mephi/iw/settings/SafetySettings.xhtml"
            , "/ru/mephi/iw/indexes/IMOEX/DownloadIMOEX.xhtml"
            , "/ru/mephi/iw/users/UsersView.xhtml", "/ru/mephi/iw/users/UserInfoChange.xhtml"
            , "/ru/mephi/iw/auth_pages/Registration.xhtml"));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (!(EXISTS_PAGES.contains(request.getRequestURI().substring(request.getContextPath().length()))
                || request.getRequestURI().contains("/javax.faces.resource/"))){

            String msg = java.net.URLEncoder.encode("Вы нашли кабинет инвестора, но не нашли страницу:(", "UTF-8");
            request.getServletContext().getRequestDispatcher("/ru/mephi/iw/error/Error.xhtml?msg=" + msg)
                    .forward(request, response);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
