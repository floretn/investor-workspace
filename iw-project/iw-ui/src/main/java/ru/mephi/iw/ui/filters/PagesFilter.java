package ru.mephi.iw.ui.filters;

import ru.mephi.iw.models.auth.Pages;
import ru.mephi.iw.ui.beans.auth_pages.Auth;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter
public class PagesFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Auth auth = (Auth) request.getSession().getAttribute("auth");

        if (auth.getCurrentUserInfo() == null || !checkPages(auth.getCurrentUserInfo().getPagesOfUser(),
                request.getRequestURI().substring(request.getContextPath().length()))) {
            response.sendRedirect(request.getContextPath() + "/ru/mephi/iw/error/Error.xhtml");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean checkPages(Set<Pages> pages, String path) {
        for (Pages page : pages) {
            if (page.getPath().contains(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
