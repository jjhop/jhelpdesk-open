package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
public class Preferences implements Serializable {

    public static Preferences getDefault() {
        return new Preferences() {{
            setWelcomePage("desktop");
            setAnnouncementsListSize(10);
            setArticlesListSize(10);
            setFiltersListSize(10);
            setNewTicketFormView("form");
            setPreferredLocale(Locale.getDefault());
            setSearchResultLimit(10);
            setTicketsListSize(10);
            setUsersListSize(10);
        }};
    }

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "preferences")
    private User user;

    @Column(name = "welcome_page")
    private String welcomePage;

    @Column(name = "locale")
    private Locale preferredLocale;

    @Column(name = "filter_id")
    private Long filterId;

    @Column(name = "new_ticket_form_view")
    private String newTicketFormView;

    @Column(name = "tickets_list_size")
    private Integer ticketsListSize;

    @Column(name = "announcements_list_size")
    private Integer announcementsListSize;

    @Column(name = "articles_list_size")
    private Integer articlesListSize;

    @Column(name = "users_list_size")
    private Integer usersListSize;

    @Column(name = "filters_list_size")
    private Integer filtersListSize;

    @Column(name = "search_result_limit")
    private Integer searchResultLimit;

}
