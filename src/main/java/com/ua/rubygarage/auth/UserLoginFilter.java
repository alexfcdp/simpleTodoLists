package com.ua.rubygarage.auth;

import com.ua.rubygarage.UserBean;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/todoLists.xhtml")
public class UserLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String urlLogin = request.getContextPath() + "/login.xhtml";
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = UserBean.isLogged();
        if (isLoggedIn && session!=null) {
          /*  if (!request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
            }*/
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            response.sendRedirect(urlLogin);
        }

    }

    @Override
    public void destroy() {

    }
}
