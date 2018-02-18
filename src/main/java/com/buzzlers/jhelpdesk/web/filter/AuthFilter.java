package com.buzzlers.jhelpdesk.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.apache.commons.lang.BooleanUtils.isFalse;

public class AuthFilter implements Filter {

    public void init(FilterConfig filter) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletReq = (HttpServletRequest) req;

        String reqUrl = servletReq.getRequestURL().toString().toLowerCase();
        if (reqUrl.endsWith("/login.html")
                || reqUrl.endsWith(".ico")
                || reqUrl.endsWith(".gif")
                || reqUrl.endsWith(".png")
                || reqUrl.endsWith(".htc")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession sess = servletReq.getSession();
        Boolean logged = (Boolean) sess.getAttribute("logged");
        if (isFalse(logged)) {
            int rp = servletReq.getContextPath().length();
            sess.setAttribute("requestURI", servletReq.getRequestURI().substring(rp));
            ((HttpServletResponse) res).sendRedirect(servletReq.getContextPath() + "/login.html");
            return;
        }
        chain.doFilter(req, res);
    }

    public void destroy() {}
}
