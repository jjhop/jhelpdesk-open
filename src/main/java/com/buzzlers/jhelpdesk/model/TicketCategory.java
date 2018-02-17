package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket_category")
@SequenceGenerator(name = "ticket_category_sequence", sequenceName = "ticket_category_id_seq", allocationSize = 1)
@Getter
@Setter
public class TicketCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_category_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "category_name", length = 64)
    private String categoryName;

    @Column(name = "category_desc", length = 256)
    private String categoryDesc;

    @Column(name = "ord")
    private Long order;
    
    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "tickets_count")
    private Long ticketsCount;

    @OneToMany(mappedBy = "ticketCategory")
    private Set<Ticket> tickets;

    public TicketCategory() {
        this.tickets = new HashSet<>();
    }

    public TicketCategory(int categoryId, String categoryName) {
        this.id = new Long(categoryId);
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object obj) {
       if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (this.id == null) {
            return false;
        }
        TicketCategory category = (TicketCategory) obj;
        return this.id.equals(category.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 67 * hash + (this.categoryName != null ? this.categoryName.hashCode() : 0);
        hash = 67 * hash + (this.categoryDesc != null ? this.categoryDesc.hashCode() : 0);
        return hash;
    }
}
