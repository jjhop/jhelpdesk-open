package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "announcement")
@SequenceGenerator(name = "announcement_sequence", sequenceName = "announcement_id_seq")
@SecondaryTable(name = "announcement_body",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"))
@NamedQueries({
    @NamedQuery(name = "Announcement.allOrderByCreatedAtDesc", query = "SELECT a FROM Announcement a ORDER BY a.createdAt DESC"),
    @NamedQuery(name = "Announcement.byId", query = "SELECT a FROM Announcement a WHERE a.id=?1")
})
@Getter
@Setter
public class Announcement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_sequence")
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User author;

    @Column(name = "title", length = 512)
    private String title;

    @Column(name = "lead", length = 4096)
    private String lead;
    
    @Column(name = "body", table = "announcement_body", nullable = true, length = 16384)
    private String body;

    public Announcement() {
        this.createdAt = new Date();
    }

}
