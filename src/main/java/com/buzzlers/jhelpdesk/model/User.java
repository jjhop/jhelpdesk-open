package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

import static com.buzzlers.jhelpdesk.model.Role.CLIENT;
import static com.buzzlers.jhelpdesk.model.Role.MANAGER;
import static com.buzzlers.jhelpdesk.model.Role.TICKETKILLER;

/**
 * Reprezentacja użytkownika w systemie. Przechowuje wszystkie istotne informacje związane z użytkownikem
 * i udostępnia je aplikacji.
 *
 * <p>
 * Klasa funkcjonuje jako <i>bean</i>, ale część danych nie powinna ulegać zmianie po pierwszym zapisaniu
 * w bazie danych. Przy polach, które nie powinny być zmieniane zostało to ujete w komentarzu przy
 * metodzie {@code setXXX(Object data)}. Oczywiście metody te są używane także podczas wiązania danych
 * z formularza z odpowiednimi polami obiektu.
 * </p>
 */
@Entity
@Table(name = "users")
@SequenceGenerator(name = "user_sequence", sequenceName = "user_id_seq", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "User.countAll",
                query = "SELECT COUNT(u) FROM User u"),
    @NamedQuery(name = "User.byEmailAndHashedPassword",
                query = "SELECT u FROM User u WHERE u.email=?1 AND u.hashedPassword=?2"),
    @NamedQuery(name = "User.byEmail",
                query = "SELECT u FROM User u WHERE u.email=?1"),
    @NamedQuery(name = "User.byEmailFetchFilters",
                query = "SELECT u FROM User u LEFT JOIN FETCH u.filters WHERE u.email=?1"),
    @NamedQuery(name = "User.allOrderByLastName",
                query = "SELECT u FROM User u ORDER by u.lastName ASC"),
    @NamedQuery(name = "User.allByRoleOrderByLastName",
                query = "SELECT u FROM User u WHERE u.roleAsInt=?1 ORDER by u.lastName ASC"),
    @NamedQuery(name = "User.activeByRoleOrderByLastName",
                query = "SELECT u FROM User u WHERE u.roleAsInt=?1 AND u.isActive=true ORDER by u.lastName ASC")
})
@Getter
@Setter
public class User implements Serializable {

	private final static int DEFAULT_LIST_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_uid")
    private UUID uuid;

    @Column(name = "passw")
    private String hashedPassword;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Transient
    private Role userRole;

    @Basic
    @Column(name = "app_role")
    private int roleAsInt;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Preferences preferences;

    @OneToMany(mappedBy = "author")
    private Set<Article> articles;

    @OneToMany(mappedBy = "owner")
    private Set<TicketFilter> filters;

    public User() {
        this.articles = new HashSet<>();
    }

    public User(Long userId, String firstName, String lastName) {
        this();
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean isTicketKiller() {
        return userRole == TICKETKILLER;
    }

    public boolean isManager() {
        return userRole == MANAGER;
    }

    public boolean isPlain() {
        return userRole == CLIENT;
    }

    public Locale getPreferredLocale() {
        return preferences != null
                ? preferences.getPreferredLocale()
                : Locale.getDefault();
    }

    public Integer getPreferedTicketsListSize() {
        return preferences != null
            ? preferences.getTicketsListSize()
            : DEFAULT_LIST_SIZE;
    }

    public Integer getAnnouncementsListSize() {
        return preferences != null
            ? preferences.getAnnouncementsListSize()
            : DEFAULT_LIST_SIZE;
    }

    public Integer getArticlesListSize() {
        return preferences != null
            ? preferences.getArticlesListSize()
            : DEFAULT_LIST_SIZE;
    }

    public Integer getFiltersListSize() {
        return preferences != null
            ? preferences.getFiltersListSize()
            : DEFAULT_LIST_SIZE;
    }

    public Integer getUsersListSize() {
        return preferences != null
            ? preferences.getUsersListSize()
            : DEFAULT_LIST_SIZE;
    }

    public int getDefaultListSize() {
        return DEFAULT_LIST_SIZE;
    }

    public int getSearchResultLimit() {
        return preferences != null
            ? preferences.getSearchResultLimit()
            : DEFAULT_LIST_SIZE;
    }

    public String getWelcomePage() {
        String welcomePage = getPreferences().getWelcomePage();
        if (welcomePage.equalsIgnoreCase("desktop")) {
            return "/desktop/main.html";
        } else if (welcomePage.equalsIgnoreCase("tickets")) {
            return "/tickets/byFilter/" + getPreferences().getFilterId() + "/list.html";
        } else if (welcomePage.equalsIgnoreCase("newTicket")) {
            String formView = getPreferences().getNewTicketFormView();
            return "form".equalsIgnoreCase(formView)
                ? "/tickets/new.html"
                : "/tickets/wizzard.html";
        } else if (welcomePage.equalsIgnoreCase("kBase")) {
            return "/help/kb/index.html";
        }
        return "/desktop/main.html";
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @PrePersist
    protected void populateRoleDB() {
        this.roleAsInt = this.userRole.toInt();
    }

    @PostLoad
    protected void populateRoleTransient() {
        this.userRole = Role.fromInt(this.roleAsInt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (this.userId == null) {
            return false;
        }
        User u = (User) obj;
        return this.userId.equals(u.userId);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == this.userId ? 0 : this.userId.hashCode());
        hash = 31 * hash + (null == this.firstName ? 0 : this.firstName.hashCode());
        hash = 31 * hash + (null == this.lastName ? 0 : this.lastName.hashCode());
        hash = 31 * hash + (null == this.email ? 0 : this.email.hashCode());
        hash = 31 * hash + (null == this.preferences ? 0 : this.preferences.hashCode());
        return hash;
    }
}
