package ru.mephi.iw.ui.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class LoginFilter implements Filter {

    /*
    /
/javax.faces.resource/primeicons/primeicons.css.xhtml
/javax.faces.resource/theme.css.xhtml
/javax.faces.resource/fa/font-awesome.css.xhtml
/javax.faces.resource/components.css.xhtml
/javax.faces.resource/jquery/jquery.js.xhtml
/javax.faces.resource/jquery/jquery-plugins.js.xhtml
/javax.faces.resource/core.js.xhtml
/javax.faces.resource/components.js.xhtml
/javax.faces.resource/validation/validation.js.xhtml
/javax.faces.resource/css/Center.css.xhtml
     */

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        System.out.println(request.getRequestURI().substring(request.getContextPath().length()));
        System.out.println(request.getRequestURI());
        if (!(request.getRequestURI().substring(request.getContextPath().length()).equals("/ru/mephi/iw/auth_pages/Auth.xhtml")
                || request.getRequestURI()
                .substring(request.getContextPath().length()).equals("/ru/mephi/iw/auth_pages/Registration.xhtml")
                || request.getRequestURI().substring(request.getContextPath().length()).equals("/")
                || request.getRequestURI().contains("/javax.faces.resource/"))) {
            if (request.getSession().getAttribute("user") == null
                    || request.getSession().getAttribute("hat") == null) {
                response.sendRedirect(request.getContextPath() + "/ru/mephi/iw/auth_pages/Auth.xhtml");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
