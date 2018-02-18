package com.buzzlers.jhelpdesk.web.tools;

import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;
import org.springframework.web.util.WebUtils;

import com.buzzlers.jhelpdesk.model.User;

public class LocaleCustomResolver extends AbstractLocaleResolver {

    public Locale resolveLocale(HttpServletRequest request) {
        Locale raLocale = (Locale) request.getAttribute("jhd_locale");
        if (raLocale != null) {
            return raLocale;
        }
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("loggedUser");
        if (currentUser != null && currentUser.getUserId() != null) {
            request.setAttribute("jhd_locale", currentUser.getPreferredLocale());
            return currentUser.getPreferredLocale();
        }
        Cookie localeCookie = WebUtils.getCookie(request, "jhd_locale");
        if (localeCookie != null) {
            Locale locale = StringUtils.parseLocaleString(localeCookie.getValue());
            if (locale != null) {
                request.setAttribute("jhd_locale", locale);
                return locale;
            }
        }
        request.setAttribute("jhd_locale", request.getLocale());
        return request.getLocale();
    }

    public void setLocale(HttpServletRequest req, HttpServletResponse res, Locale locale) {
    }
}
