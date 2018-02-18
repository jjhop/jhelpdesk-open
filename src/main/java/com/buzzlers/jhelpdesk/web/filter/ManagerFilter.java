package com.buzzlers.jhelpdesk.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.buzzlers.jhelpdesk.model.Role;
import com.buzzlers.jhelpdesk.model.User;
import com.buzzlers.jhelpdesk.web.NotAuthorizedAccessException;

public class ManagerFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession sess = req.getSession();
        User user = (User) sess.getAttribute("loggedUser");
        if (user == null) {
            throw new NotAuthorizedAccessException("not authorized access!");
        }
        if (user.getUserRole().toInt() < Role.MANAGER.toInt()) {
            throw new NotAuthorizedAccessException("not authorized access!");
        }
        chain.doFilter(request, response);
    }
    public void destroy() {}
}
