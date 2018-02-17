package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article")
@SequenceGenerator(name = "article_sequence", sequenceName = "article_id_seq", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "Article.lastAdded", query = "SELECT a FROM Article a ORDER BY a.createdAt DESC"),
    @NamedQuery(name = "Article.getForCategory", 
        query = "SELECT a FROM Article a WHERE a.category.id=?1 ORDER BY a.createdAt DESC")
})
@Getter
@Setter
public class Article implements Serializable {

    private static final long serialVersionUID = 6398694944488620526L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="article_sequence")
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private ArticleCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "ord")
    private Long order;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "lead", length = 4096)
    private String lead;

    @Column(name = "body", length = 16384)
    private String body;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.REMOVE})
    @OrderBy(value = "createdAt")
    private List<ArticleComment> comments;

    @ManyToMany
    @JoinTable(name = "article_ticket",
        joinColumns = {@JoinColumn(name = "article_id")},
        inverseJoinColumns = {@JoinColumn(name = "ticket_id")})
    private Set<Ticket> associatedTickets;

    public Article() {
        this.comments = new ArrayList<>();
        this.associatedTickets = new HashSet<>();
        this.createdAt = new Date();
    }

    public Article(Long id, String title, String lead, Date createdAt) {
        this();
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.lead = lead;
    }

    public boolean isAssociatedWithTicket(Long ticketId) {
        assert ticketId != null;
        for (Ticket ticket : associatedTickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                return true;
            }
        }
        return false;
    }
}
