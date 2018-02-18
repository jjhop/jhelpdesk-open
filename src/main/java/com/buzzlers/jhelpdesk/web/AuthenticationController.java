package com.buzzlers.jhelpdesk.web;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.http.HttpSession;
import static java.lang.Boolean.TRUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.buzzlers.jhelpdesk.dao.UserDAO;
import com.buzzlers.jhelpdesk.model.User;

@Controller
public class AuthenticationController {

    private final UserDAO userDAO;

    @Autowired
    public AuthenticationController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("redirect:/desktop/main.html");
    }

    @GetMapping("/login.html")
    public ModelAndView setupLoginForm(HttpSession session) {
        User loggedUser;
        boolean isLooged = session.getAttribute("logged") != null && (Boolean) session.getAttribute("logged");
        if (isLooged && (loggedUser = (User) session.getAttribute("loggedUser")) != null) {
            return new ModelAndView("redirect:" + loggedUser.getWelcomePage());
        }

        return new ModelAndView("login") {{
            addObject("user", new User());
        }};
    }

    @PostMapping("/login.html")
    public ModelAndView processLogin(@ModelAttribute("user") User user,
                                     HttpSession session) throws Exception {

        // TODO: w DAO metoda authenticate do wywalenia... uwierzytleniamy sprawdzajac
        // czy gosc podal pasujace haslo i login (email) oraz czy moze sie logowac (isActive)
        boolean isAuthenticated = userDAO.authenticate(user.getEmail(), user.getHashedPassword());

        if (isAuthenticated) {
            User loggedUser = userDAO.getByEmailFetchFilters(user.getEmail());
            session.setAttribute("loggedUser", loggedUser);
            session.setAttribute("logged", TRUE);
            session.setAttribute("paths", new ConcurrentLinkedQueue<String>());
            String requestURI = (String) session.getAttribute("requestURI");
            session.removeAttribute("requestURI");
            String resultView = "redirect:" + (isTargetURIValid(requestURI)
                                    ? requestURI
                                    : loggedUser.getWelcomePage());
            return new ModelAndView(resultView);
        }
        return new ModelAndView("login") {{
            addObject("badLogin", TRUE);
        }};
    }

    @GetMapping("/logout.html")
    public String processLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.html";
    }

    private boolean isTargetURIValid(String targetURI) {
        return targetURI != null && targetURI.length() > 0 && !targetURI.contains("logout.html");
    }
}
