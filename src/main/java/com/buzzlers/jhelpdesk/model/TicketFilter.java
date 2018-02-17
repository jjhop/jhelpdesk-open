package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import static java.util.Collections.emptyList;

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
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.buzzlers.jhelpdesk.utils.StampUtils;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket_filters")
@SequenceGenerator(name = "ticket_filter_sequence", sequenceName = "ticket_filter_id_seq")
@NamedQueries({
    @NamedQuery(name = "TicketFilter.forUserOrderByNameASC", query = "SELECT tf FROM TicketFilter tf WHERE tf.owner=?1")
})
@Getter
@Setter
public class TicketFilter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_filter_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "tfstamp")
    private String tfStamp;
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "begin_date", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToMany
    @JoinTable(name="ticket_filters_ticket_categories",
               joinColumns={@JoinColumn(name="ticket_filter_id")},
               inverseJoinColumns={@JoinColumn(name="ticket_category_id")})
    private List<TicketCategory> ticketCategories;

    @ManyToMany
    @JoinTable(name="ticket_filters_notifiers",
               joinColumns={@JoinColumn(name="ticket_filter_id")},
               inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> notifiers;

    @ManyToMany
    @JoinTable(name="ticket_filters_saviours",
               joinColumns={@JoinColumn(name="ticket_filter_id")},
               inverseJoinColumns={@JoinColumn(name="user_id")})
    private List<User> saviours;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User owner;

    @Transient
    private List<TicketPriority> ticketPriorities;

    @Column(name = "priorities")
    private String ticketPrioritiesAsString;

    @Transient
    private List<TicketStatus> ticketStatuses;

    @Column(name = "statuses")
    private String ticketStatusesAsString;

    public TicketFilter() {
        this.createdAt = new Date();
    }

    public boolean isOwnedBy(User user) {
        return this.owner.equals(user);
    }

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories == null
            ? emptyList()
            : ticketCategories;
    }

    public List<TicketPriority> getTicketPriorities() {
        return ticketPriorities == null
            ? emptyList()
            : ticketPriorities;
    }

    public void setTicketPriorities(List<TicketPriority> ticketPriorities) {
        this.ticketPriorities = ticketPriorities;
        this.ticketPrioritiesAsString = TicketPriority.listAsString(this.ticketPriorities);
    }

    public List<TicketStatus> getTicketStatuses() {
        return ticketStatuses == null
            ? emptyList()
            : ticketStatuses;
    }

    public void setTicketStatuses(List<TicketStatus> ticketStatuses) {
        this.ticketStatuses = ticketStatuses;
        this.ticketStatusesAsString = TicketStatus.listAsString(this.ticketStatuses);
    }

    public List<User> getNotifiers() {
       return notifiers == null
            ? emptyList()
            : notifiers;
    }

    public List<User> getSaviours() {
        return saviours == null
            ? emptyList()
            : saviours;
    }

    @PreUpdate
    protected void preUpdate() {
        this.tfStamp = createUniqueStamp();
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = new Date();
        this.tfStamp = createUniqueStamp();
    }

    @PostLoad
    protected void postLoad() {
        this.ticketPriorities = TicketPriority.listFromString(this.ticketPrioritiesAsString);
        this.ticketStatuses   = TicketStatus.listFromString(this.ticketStatusesAsString);
    }

    private String createUniqueStamp() {
        return StampUtils.craeteStampFromObjects(owner.getUserId(), owner.toString(), name);
    }
}
