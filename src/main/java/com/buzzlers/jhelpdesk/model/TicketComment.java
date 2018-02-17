package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "ticket_comment")
@SequenceGenerator(name = "ticket_comment_sequence", sequenceName = "ticket_comment_id_seq", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "TicketComment.getCommentsForTicketOrderByCreatedAtDESC",
                query = "SELECT t FROM TicketComment t WHERE t.ticket.ticketId=?1 ORDER BY t.commentDate DESC"),
    @NamedQuery(name = "TicketComment.countCommentsForTicket", query = "SELECT COUNT(tc) FROM TicketComment tc WHERE tc.ticket.ticketId=?1")
})
@Getter
@Setter
public class TicketComment implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ticket_comment_sequence")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User commentAuthor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "comment_date")
    private Date commentDate;

    @Column(name = "comment_text", length = 4096)
    private String commentText;

    @Transient
    private CommentType commentType;

    @Basic
    @Column(name = "comment_type")
    private int commentTypeAsInt;

    @Column(name="not_for_plain_user")
    private boolean notForPlainUser;

    public TicketComment() {
        this.notForPlainUser = true;
        this.commentDate = new Date();
    }

    @PrePersist
    protected void populateCommentTypeDB() {
        this.commentTypeAsInt = this.commentType.toInt();
    }

    @PostLoad
    protected void populateCommentTypeTransient() {
        this.commentType = CommentType.fromInt(commentTypeAsInt);
    }
}
