package ru.mephi.iw.ui.filters;

import ru.mephi.iw.models.auth.Pages;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;

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

        CurrentUserInfo currentUserInfo = (CurrentUserInfo) request.getSession().getAttribute("user");

        if (currentUserInfo == null || !checkPages(currentUserInfo.getPagesOfUser(),
                request.getRequestURI().substring(request.getContextPath().length()))) {

            String msg = java.net.URLEncoder.encode("У вас недостаточно прав для перехода на страницу!", "UTF-8");

            request.getServletContext().getRequestDispatcher("/ru/mephi/iw/error/Error.xhtml?msg=" + msg)
                    .forward(request, response);
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
