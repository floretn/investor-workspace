package ru.mephi.iw.ui.filters;

import org.apache.poi.hssf.record.UserSViewBegin;
import ru.mephi.iw.ui.beans.auth_pages.Auth;
import ru.mephi.iw.ui.beans.views.UsersView;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class UICFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        UsersView usersView = (UsersView) request.getSession().getAttribute("usersView");

        if (usersView == null || usersView.getSelectedUserForUpdate() == null) {
            response.sendRedirect(request.getContextPath() + "/ru/mephi/iw/views/UsersView.xhtml");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
