package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import static javax.persistence.CascadeType.ALL;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Klasa będąca osią aplikacji. Modeluje zgłoszenie problemu do działu wsparcia w rzeczywistym świecie.
 * I jako takie ma związane ze sobą podstawowe informacje takie jak osoba, która problem zgłosiła,
 * datę zgłoszenia itd.
 * </p>
 * <p>
 * Klasa funkcjonuje jako <i>bean</i>, ale większość danych nie powinna ulegać zmianie po pierwszym zapisaniu
 * zgłoszenia w bazie danych. Przy polach, które nie powinny być zmieniane zostało to ujete w komentarzu przy
 * metodzie {@code setXXX(Object data)}. Oczywiście metody te są używane także podczas wiązania danych
 * z formularza z odpowiednimi polami obiektu.
 * </p>
 */
@Entity
@Table(name = "ticket")
@SequenceGenerator(name = "ticket_sequence", sequenceName = "ticket_id_seq", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "Ticket.orderByCreateDateDESC", query = "SELECT t FROM Ticket t ORDER BY t.createdAt DESC"),
    @NamedQuery(name = "Ticket.byStatusOrderByCreateDateDESC", query = "SELECT t FROM Ticket t WHERE t.ticketStatusAsInt=?1 ORDER BY t.createdAt DESC"),
    @NamedQuery(name = "Ticket.allByCategory", query = "SELECT t FROM Ticket t WHERE t.ticketCategory=?1 ORDER BY t.createdAt DESC"),
    @NamedQuery(name = "Ticket.allByPriority", query = "SELECT t FROM Ticket t WHERE t.ticketPriorityAsInt=?1 ORDER BY t.createdAt DESC"),
    @NamedQuery(name = "Ticket.allByStatus", query = "SELECT t FROM Ticket t WHERE t.ticketStatusAsInt=?1 ORDER BY t.createdAt DESC"),
    @NamedQuery(name = "Ticket.allByNotifier", query = "SELECT t FROM Ticket t WHERE t.notifier=?1 ORDER BY t.createdAt DESC")
})
@Getter
@Setter
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ticket_sequence")
    @Column(name = "id")
    private Long ticketId;

    @Transient
    private String ticketstamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "notifier", referencedColumnName = "user_id")
    private User notifier;

    @ManyToOne(optional = true)
    @JoinColumn(name = "saviour", referencedColumnName = "user_id")
    private User saviour;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inputer", referencedColumnName = "user_id")
    private User inputer;

    @Column(name = "subject", length = 512)
    private String subject;

    @Column(name = "description", length = 16384)
    private String description;

    @Column(name = "step_by_step", length = 16384)
    private String stepByStep;

    @Transient
    private TicketStatus ticketStatus;

    @Basic
    @Column(name = "status")
    private int ticketStatusAsInt;

    @Transient
    private TicketPriority ticketPriority;

    @Basic
    @Column(name = "priority")
    private int ticketPriorityAsInt;

    @ManyToOne
    @JoinColumn(name = "ticket_category")
    private TicketCategory ticketCategory;
    
    @OneToMany(mappedBy = "ticket", cascade = ALL)
    private Set<TicketComment> comments;
    
    @OneToMany(mappedBy = "ticket", cascade = ALL)
    private Set<TicketEvent> events;

    @OneToMany(mappedBy = "ticket", cascade = ALL)
    private List<AdditionalFile> addFilesList;

    @ManyToMany(mappedBy="associatedTickets")
    private Set<Article> articles;

    public static Ticket create(TicketPriority priority, TicketCategory category,
                                String subject, String description,
                                List<AdditionalFile> additionalFiles,
                                User notifier, User inputer) {
        return new Ticket() {{
            setTicketStatus(TicketStatus.NOTIFIED);
            setTicketPriority(priority);
            setNotifier(notifier);
            setInputer(inputer);
            setTicketCategory(category);
            setSubject(subject);
            setDescription(description);
            setAddFilesList(additionalFiles);
        }};
    }

    public static Ticket create(TicketPriority priority, TicketCategory category,
                                  String subject, String description,
                                  List<AdditionalFile> additionalFiles,
                                  User notifier) {
        return create(priority, category, subject, description, additionalFiles, notifier, notifier);
    }
    
    public Ticket() {
        this.comments = new HashSet<>();
        this.addFilesList = new ArrayList<>();
        this.articles = new HashSet<>();
        this.events = new HashSet<>();
        this.ticketStatus = TicketStatus.NOTIFIED;
        this.createdAt = new Date();
    }

    public String getShortSubject() {
        if (subject.length() > 128) {
            String s = subject.substring(0, 128);
            int lastSpace = s.lastIndexOf(' ');
            return s.substring(0, lastSpace);
        }
        return subject;
    }

    public void addComment(TicketComment comm) {
        this.comments.add(comm);
    }

    public boolean isAssigned() {
        return saviour != null;
    }

    @PrePersist
    protected void populateTicketEnumsDB() {
        this.ticketStatusAsInt = this.ticketStatus.toInt();
        this.ticketPriorityAsInt = this.ticketPriority.toInt();
    }

    @PostLoad
    protected void populateTicketEnumsTransient() {
        this.ticketStatus = TicketStatus.fromInt(this.ticketStatusAsInt);
        this.ticketPriority = TicketPriority.fromInt(this.ticketPriorityAsInt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID:       ").append(this.ticketId).append("\n");
        sb.append("CREATED:  ").append(this.createdAt).append("\n");
        sb.append("SUBJECT:  ").append(this.subject).append("\n");
        sb.append("PRIORITY: ").append(this.ticketPriority).append("\n");
        sb.append("STATUS:   ").append(this.ticketStatus).append("\n");
        return sb.toString();
    }
}
