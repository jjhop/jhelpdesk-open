package com.buzzlers.jhelpdesk.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.buzzlers.jhelpdesk.dao.ArticleCategoryDAO;
import com.buzzlers.jhelpdesk.dao.ArticleDAO;
import com.buzzlers.jhelpdesk.dao.TicketDAO;
import com.buzzlers.jhelpdesk.model.Article;
import com.buzzlers.jhelpdesk.model.ArticleComment;
import com.buzzlers.jhelpdesk.model.Ticket;
import com.buzzlers.jhelpdesk.model.User;
import com.buzzlers.jhelpdesk.web.search.LuceneIndexer;
import com.buzzlers.jhelpdesk.web.search.SearchException;
import com.buzzlers.jhelpdesk.web.tools.ArticleCommentValidator;
import static com.buzzlers.jhelpdesk.web.commons.PagingTools.*;

@Controller
public class KBViewController {

    private final static Logger LOG = LoggerFactory.getLogger(KBViewController.class);

    private final static int NUM_OF_LAST_ADDED_ARTICLES = 10;

    private final static String HELP_ABOUT = "help/about";
    private final static String HELP_KB_INDEX = "help/kb/index";
    private final static String HELP_KB_CATEGORY = "help/kb/category";
    private final static String HELP_KB_ARTICLE = "help/kb/one";
    private final static String HELP_KB_SEARCH_RESULT = "help/kb/searchResult";
    private final static String HELP_KB_COMMENT_FORM = "/help/kb/comment/form";
    private final static String HELP_KB_COMMENT_RESULT = "/help/kb/comment/result";
    private final static String HELP_KB_TICKET_ASSIGN_FORM = "/help/kb/ticketAssign/form";
    private final static String HELP_KB_TICKET_ASSIGN_RESULT = "/help/kb/ticketAssign/result";
    private final static String HELP_KB_TICKET_SEARCH = "/help/base/articles/searchTickets";

    private final TicketDAO ticketDAO;
    private final ArticleDAO articleDAO;
    private final ArticleCategoryDAO articleCategoryDAO;
    private final ArticleCommentValidator validator;
    private final LuceneIndexer luceneIndexer;

    @Autowired    
    public KBViewController(TicketDAO ticketDAO, ArticleDAO articleDAO,
                            ArticleCategoryDAO articleCategoryDAO,
                            ArticleCommentValidator validator,
                            LuceneIndexer luceneIndexer) {
		this.ticketDAO = ticketDAO;
		this.articleDAO = articleDAO;
		this.articleCategoryDAO = articleCategoryDAO;
		this.validator = validator;
		this.luceneIndexer = luceneIndexer;
	}

    @RequestMapping("/help/about.html")
    public String aboutView() {
        return HELP_ABOUT;
    }

    @RequestMapping(value = "/help/kb/index.html", method = RequestMethod.GET)
    public ModelAndView kBView() throws Exception {
        return new ModelAndView(HELP_KB_INDEX) {{
            addObject("categories", articleCategoryDAO.getAllCategories());
            addObject("latest", articleDAO.getLastArticles(NUM_OF_LAST_ADDED_ARTICLES));
        }};
    }

    @RequestMapping(value = "/help/kb/search.html", method = RequestMethod.GET)
    public ModelAndView kBSearch(@RequestParam("query") String query, HttpSession session) throws Exception {

        ModelAndView res = new ModelAndView();
        try {
            User currentUser = (User) session.getAttribute("loggedUser");
            List<Article> result = luceneIndexer.search(query, currentUser.getSearchResultLimit());
            res.addObject("result", result);
            if (result.size() < 1) {
                res.addObject("categories", articleCategoryDAO.getAllCategories());
                res.addObject("latest", articleDAO.getLastArticles(NUM_OF_LAST_ADDED_ARTICLES));
                res.addObject("msg", "kb.search.notfound.error");
                res.setViewName(HELP_KB_INDEX);
                return res;
            }
        } catch(SearchException se) {
            res.addObject("categories", articleCategoryDAO.getAllCategories());
            res.addObject("latest", articleDAO.getLastArticles(NUM_OF_LAST_ADDED_ARTICLES));
            res.addObject("msg", "kb.search.string.format.error");
            res.setViewName(HELP_KB_INDEX);
            return res;
        }
        res.setViewName(HELP_KB_SEARCH_RESULT);
        return res;
    }

    @RequestMapping(value = "/help/base/category/{id}/show.html", method = RequestMethod.GET)
    public ModelAndView kBCategoryView(@RequestParam(value = "p", required = false, defaultValue = "1") int page,
                                       @PathVariable("id") Long cId, HttpSession session) throws Exception {
        User currentUser = (User) session.getAttribute("loggedUser");
        int pageSize = currentUser.getArticlesListSize();
        int articlesInSection = articleDAO.countForSection(cId);
        int offset = calculateOffset(pageSize, page);

        return new ModelAndView(HELP_KB_CATEGORY) {{
            addObject("category", articleCategoryDAO.getById(cId));
            addObject("articles", articleDAO.getForSection(cId, pageSize, offset));
            addObject("currentPage", page);
            addObject("pages", calculatePages(articlesInSection, pageSize));
            addObject("offset", offset);
        }};
    }

    @RequestMapping(value = "/help/base/articles/{aId}/show.html", method = RequestMethod.GET)
    public String kBItemView(@PathVariable("aId") Long id, ModelMap map) throws Exception {
        Article article = articleDAO.getById(id);
        if (article != null) {
            map.addAttribute("article", article);
        } else {
            map.addAttribute("message", "Nie znaleziono");
        }
        map.addAttribute("comment", new ArticleComment());
        return HELP_KB_ARTICLE;
    }

    @RequestMapping(value = "/help/base/articles/{aId}/comments/new.html", method = RequestMethod.GET)
    public String prepareCommentForm(@PathVariable("aId") Long articleId, ModelMap map) {
        map.addAttribute("comment", new ArticleComment());
        map.addAttribute("articleId", articleId);
        return HELP_KB_COMMENT_FORM;
    }

    @RequestMapping(value = "/help/base/articles/{aId}/comments/new.html", method = RequestMethod.POST)
    public String processCommentForm(@PathVariable("aId") Long articleId,
                                     @ModelAttribute("comment") ArticleComment comment,
                                     BindingResult errors, ModelMap map, HttpSession session) throws Exception {
        validator.validate(comment, errors);
        if (errors.hasErrors()) {
            map.addAttribute("comment", comment);
            return HELP_KB_COMMENT_FORM;
        }
        comment.setCreatedAt(new Date());
        comment.setArticle(articleDAO.getById(articleId));
        comment.setAuthorId((User) session.getAttribute("loggedUser"));
        articleDAO.saveArticleComment(comment);
        return HELP_KB_COMMENT_RESULT;
    }

    @RequestMapping(value = "/help/base/articles/{aId}/tickets/new.html", method = RequestMethod.GET)
    public String prepareArticleTicketForm(@PathVariable("aId") Long articleId,
                                           @RequestParam(value = "tId", required = false) Long ticketId,
                                           ModelMap map) throws Exception {
        if (ticketId != null) {
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket != null) {
                map.addAttribute("ticket", ticket);
            } else {
                map.addAttribute("message", "Nie znaleziono");
            }
        }
        map.addAttribute("articleId", articleId);
        return HELP_KB_TICKET_ASSIGN_FORM;
    }

    @RequestMapping(value = "/help/base/articles/{aId}/tickets/new.html", method = RequestMethod.POST)
    public String processArticleTicketForm(@PathVariable("aId") Long articleId,
                                           @RequestParam(value = "tId") Long ticketId,
                                           ModelMap map) throws Exception {

        Article article = articleDAO.getById(articleId);
        if (article != null && !article.isAssociatedWithTicket(ticketId)) {
            articleDAO.assignWithTicket(articleId, ticketId);
            map.addAttribute("success", Boolean.TRUE);
        } else {
            map.addAttribute("message", "Nie można powiązać zgłoszenia ze wskazanych artykułem.");
        }
        return HELP_KB_TICKET_ASSIGN_RESULT;
    }

    @RequestMapping("/help/base/articles/{aId}/searchTickets.html")
    public String searchTickets(@PathVariable("aId") Long articleId,
                                @RequestParam(value = "q", defaultValue = "") String query,
                                ModelMap map) throws Exception {
        map.addAttribute("article", articleDAO.getById(articleId));
        if (query.startsWith("#")) {
            try {
                long ticketId = Long.parseLong(query.substring(1));
                List<Ticket> result = new ArrayList<>();
                Ticket ticket = ticketDAO.getTicketById(ticketId);
                if (ticket != null) {
                    result.add(ticket);
                }
                map.addAttribute("resultList", result);
                return HELP_KB_TICKET_SEARCH;
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
                return HELP_KB_TICKET_SEARCH;
            }
        }

        int count = ticketDAO.countWithQuery(query);
        if (count > 0) {
            List<Ticket> result = ticketDAO.searchWithQuery(query);
            map.addAttribute("resultList", result);
            if (count > result.size()) {
                map.addAttribute("moreResultCount", count - result.size());
            }
        }
        return HELP_KB_TICKET_SEARCH;
    }
}
