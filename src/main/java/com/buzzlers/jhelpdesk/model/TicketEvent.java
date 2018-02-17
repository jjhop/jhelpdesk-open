package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket_event")
@SequenceGenerator(name = "ticket_event_sequence", sequenceName = "ticket_event_id_seq", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "TicketEvent.getByTicketOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t WHERE t.ticket=?1 ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "SELECT t TicketEvent.getByTicketIdOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t WHERE t.ticket.ticketId=?1 ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "TicketEvent.getByEventTypeOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t WHERE t.eventTypeAsInt=?1 ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "TicketEvent.getByUserOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t WHERE t.evtAuthor=?1 ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "TicketEvent.getByUserIdOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t WHERE t.evtAuthor.userId=?1 ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "TicketEvent.getLastFewEventsOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "TicketEvent.getEventsForTicketOrderByEventDateDESC",
                query = "SELECT t FROM TicketEvent t WHERE t.ticket.ticketId=?1 ORDER BY t.evtDate DESC"),
    @NamedQuery(name = "TicketEvent.countEventsForTicket", query = "SELECT COUNT(evt) FROM TicketEvent evt WHERE evt.ticket.ticketId=?1")
})
@Getter
@Setter
public class TicketEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ticket_event_sequence")
    @Column(name = "id")
    private Long ticketEventId;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_date")
    private Date evtDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User evtAuthor;

    @Column(name = "event_data")
    private String eventData;

    @Transient
    private EventType eventType;

    @Basic
    @Column(name = "event_type")
    private int eventTypeAsInt;

    public static TicketEvent ticketCreated(Ticket ticket) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.CREATE);
        event.setEvtAuthor(ticket.getInputer());
        event.setEvtDate(new Date());
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketAssigned(Ticket ticket, User assigner) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.ASSIGN);
        event.setEvtAuthor(assigner);
        event.setEvtDate(new Date());
        event.setEventData(assigner);
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketAssigned(Ticket ticket, User saviour, User assigner) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.ASSIGN);
        event.setEvtAuthor(assigner);
        event.setEvtDate(new Date());
        event.setEventData(saviour);
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketReassigned(Ticket ticket, User assigner) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.REASSIGN);
        event.setEvtAuthor(assigner);
        event.setEvtDate(new Date());
        event.setEventData(assigner);
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketReassigned(Ticket ticket, User saviour, User assigner) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.REASSIGN);
        event.setEvtAuthor(assigner);
        event.setEvtDate(new Date());
        event.setEventData(saviour);
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketResolved(Ticket ticket, User user) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.RESOLVE);
        event.setEvtAuthor(user);
        event.setEvtDate(new Date());
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketRejected(Ticket ticket, User user) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.REJECT);
        event.setEvtAuthor(user);
        event.setEvtDate(new Date());
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketClosed(Ticket ticket, User user) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.CLOSE);
        event.setEvtAuthor(user);
        event.setEvtDate(new Date());
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent ticketReopened(Ticket ticket, User user) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.REOPEN);
        event.setEvtAuthor(user);
        event.setEvtDate(new Date());
        event.setTicket(ticket);
        return event;
    }

    public static TicketEvent commentAdded(TicketComment comment) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.COMMENTADD);
        event.setEvtAuthor(comment.getCommentAuthor());
        event.setEvtDate(new Date());
        event.setTicket(comment.getTicket());
        return event;
    }

    public static TicketEvent categoryChange(Ticket ticket, User author, TicketCategory category) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.CATEGORYCHANGE);
        event.setEvtAuthor(author);
        event.setEvtDate(new Date());
        event.setEventData(category);
        event.setTicket(ticket);
        return event;
    }

    public static Object priorityChange(Ticket ticket, User author, TicketPriority priority) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.PRIORITYCHANGE);
        event.setEvtAuthor(author);
        event.setEvtDate(new Date());
        event.setEventData(priority);
        event.setTicket(ticket);
        return event;
    }
    
    public static TicketEvent attachmentAdded(AdditionalFile addFile) {
        TicketEvent event = new TicketEvent();
        event.setEventType(EventType.ATTACHMENTADD);
        event.setEvtAuthor(addFile.getCreator());
        event.setEvtDate(new Date());
        event.setEventData(addFile);
        event.setTicket(addFile.getTicket());
        return event;
    }

    public void setEventData(Object data) {
        if (data instanceof User) {
            this.eventData = ((User)data).getFullName();
        } else if (data instanceof AdditionalFile) {
            this.eventData = ((AdditionalFile) data).getOriginalFileName();
        } else if (data instanceof TicketCategory) {
            this.eventData = ((TicketCategory)data).getCategoryName();
        } else if(data instanceof TicketPriority) {
            this.eventData = String.valueOf(((TicketPriority) data).toInt());
        }
    }

    public String getEvtSubject(Locale locale) {
        ResourceBundle names = ResourceBundle.getBundle("eventType", locale);
        switch (getEventType()) {
            case ASSIGN:
                return String.format(locale,
                                     names.getString("ticketEvent.assign"),
                                     ticket.getTicketId(), eventData, evtAuthor);
            case CATEGORYCHANGE:
                return String.format(locale,
                                     names.getString("ticketEvent.category.change"),
                                     evtAuthor, ticket.getTicketId());
            case CLOSE:
                return String.format(locale,
                                     names.getString("ticketEvent.close"),
                                     evtAuthor, ticket.getTicketId());
            case COMMENTADD:
                return String.format(locale,
                                     names.getString("ticketEvent.comment.add"),
                                     evtAuthor, ticket.getTicketId());
            case CREATE:
                return String.format(locale,
                                     names.getString("ticketEvent.create"),
                                     evtAuthor);
            case PRIORITYCHANGE:
                return String.format(locale,
                                     names.getString("ticketEvent.priority.change"),
                                     evtAuthor, ticket.getTicketId());
            case REASSIGN:
                return String.format(locale,
                                     names.getString("ticketEvent.reassign"),
                                     evtAuthor, ticket.getTicketId(), eventData);
            case RESOLVE:
                return String.format(locale,
                                     names.getString("ticketEvent.resolve"),
                                     evtAuthor, ticket.getTicketId());
            case REOPEN:
                return String.format(locale,
                                     names.getString("ticketEvent.reopen"),
                                     evtAuthor, ticket.getTicketId());
            case REJECT:
                return String.format(locale,
                                     names.getString("ticketEvent.reject"),
                                     evtAuthor, ticket.getTicketId());
            case STATUSCHANGE:
                return String.format(locale,
                                     names.getString("ticketEvent.status.change"),
                                     ticket.getTicketId());
            case ATTACHMENTADD:
                return String.format(locale,
                                     names.getString("ticketEvent.attachmentAdd"),
                                     evtAuthor, eventData, ticket.getTicketId());
        }
        throw new RuntimeException("Unknown event type.");
    }

    @PrePersist
    protected void populateEventTypeDB() {
        this.eventTypeAsInt = this.eventType.toInt();
    }

    @PostLoad
    protected void populateEventTypeTransient() {
        this.eventType = EventType.fromInt(this.eventTypeAsInt);
    }
}
