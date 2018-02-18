package com.buzzlers.jhelpdesk.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.buzzlers.jhelpdesk.dao.AnnouncementDAO;
import com.buzzlers.jhelpdesk.dao.ArticleDAO;
import com.buzzlers.jhelpdesk.dao.TicketDAO;
import static com.buzzlers.jhelpdesk.model.TicketStatus.NOTIFIED;

@Controller
@RequestMapping("/desktop")
public class DesktopViewController {

    private static int NUMBER_OF_EVENTS_IN_DESKTOP = 5;
    private static int NUMBER_OF_NONASSIGNED_TICKETS = 5;
    private static int NUMBER_OF_LAST_ADDED_ARTICLES = 3;
    private static int NUMBER_OF_LAST_ANNOUNCEMENTS = 3;

    private final TicketDAO ticketDAO;
    private final ArticleDAO articleDAO;
    private final AnnouncementDAO announcementDAO;

    @Autowired
    public DesktopViewController(TicketDAO ticketDAO, 
                                 ArticleDAO articleDAO,
                                 AnnouncementDAO announcementDAO) {
		this.ticketDAO = ticketDAO;
		this.articleDAO = articleDAO;
		this.announcementDAO = announcementDAO;
	}

    @GetMapping("/main.html")
    public ModelAndView showDesktop() throws Exception {
        return new ModelAndView("/desktop/main") {{
            addObject("lastArticles", articleDAO.getLastArticles(NUMBER_OF_LAST_ADDED_ARTICLES));
            addObject("announcements", announcementDAO.getLastAnnouncements(NUMBER_OF_LAST_ANNOUNCEMENTS));
            addObject("lastEvents", ticketDAO.getLastEvents(NUMBER_OF_EVENTS_IN_DESKTOP));
            addObject("lastTickets", ticketDAO.getTicketsByStatus(NOTIFIED, NUMBER_OF_NONASSIGNED_TICKETS));
        }};
    }

    @GetMapping("/lastTickets.html")
    public ModelAndView lastTickets() throws Exception {
        return new ModelAndView("/desktop/lastTickets") {{
            addObject("lastTickets",
                      ticketDAO.getTicketsByStatus(NOTIFIED, NUMBER_OF_NONASSIGNED_TICKETS));
        }};
    }

    @GetMapping("/lastEvents.html")
    public ModelAndView lastEvents() throws Exception {
        return new ModelAndView("/desktop/lastEvents") {{
            addObject("lastEvents", ticketDAO.getLastEvents(NUMBER_OF_EVENTS_IN_DESKTOP));
        }};
    }

    @GetMapping("/lastArticles.html")
    public ModelAndView lastArticles() throws Exception {
        return new ModelAndView("/desktop/lastArticles") {{
            addObject("lastArticles", articleDAO.getLastArticles(NUMBER_OF_LAST_ADDED_ARTICLES));
        }};
    }

    @GetMapping("/lastAnnouncements.html")
    public ModelAndView lastAnnouncements(ModelMap map) throws Exception {
        return new ModelAndView("/desktop/lastAnnouncements") {{
            addObject("announcements", announcementDAO.getLastAnnouncements(NUMBER_OF_LAST_ANNOUNCEMENTS));
        }};
    }
}
