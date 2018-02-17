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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import com.buzzlers.jhelpdesk.web.ticket.FileInfo;

/**
 * Pliki w systemie, które są dołączane do zgłoszenia. Do każdego zgłoszenia można
 * dołączyć dowolną (ograniczoną przestrzenią dyskową) ilość plików. W bazie danych
 * przechowujemy informacje o plikach, same zaś plik we wskazanym katalogu. Pliki
 * z założenia mają pomóc osobie zajmującej się zgłoszeniem rozwiązać problem.
 */
@Entity
@Table(name = "ticket_additional_file")
@SequenceGenerator(name = "ticket_add_files_id_seq",
    sequenceName= "ticket_add_files_id_seq", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "AdditionalFile.countAttachmentsForTicket", 
                query = "SELECT COUNT(f) FROM AdditionalFile f WHERE f.ticket.ticketId=?1"),
    @NamedQuery(name = "AdditionalFile.getAttachmentsForTicketOrderByEventDateDESC", 
                query = "SELECT f FROM AdditionalFile f WHERE f.ticket.ticketId=?1 ORDER BY f.createdAt DESC")
})
@Getter
@Setter
public class AdditionalFile implements Serializable {

    public static AdditionalFile create(String fileName, String contentType, long size,
                                        Ticket ticket) {
        return new AdditionalFile() {{
            setOriginalFileName(fileName);
            setContentType(contentType);
            setFileSize(size);
            setTicket(ticket);
            setCreatedAt(new Date());
            calculateAndSetHash();
        }};
    }
    
    public static AdditionalFile create(FileInfo info, Ticket ticket) {
    	return new AdditionalFile() {{
    	    setOriginalFileName(info.getFilename());
            setContentType(info.getContentType());
            setFileSize(info.getSize());
    	    setTicket(ticket);
            setCreatedAt(new Date());
            calculateAndSetHash();
        }};
	}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ticket_add_files_id_seq")
    @Column(name = "id")
    private Long fileId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User creator;

    @Column(name = "original_filename", length = 128, nullable = false)
    private String originalFileName;
    
    @Column(name = "digest", length = 62, nullable = false)
    private String digest;

    /**
     * Skrót z nazwy pliku i identyfikatora. Pozwala przechowywać wiele plików
     * o tej samej nazwie w jednym miejscu nie generując przy tym problemów.
     */
    @Column(name = "hashed_filename", length = 64, nullable = false, unique = true)
    private String hashedFileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    /**
     * mime/type (image/x-png, application/x-word).
     */
    @Column(name = "content_type", length = 64, nullable = false)
    private String contentType;
        
    public String getContentTypeClass() {
        if (contentType.contains("pdf")) {
            return "PDF";
        }
        if (contentType.contains("msword")) {
            return "MSWORD";
        }
        if (contentType.contains("zip")) {
            return "ARCHIVE";
        }
        if (contentType.equalsIgnoreCase("text/plain")) {
            return "TEXT";
        }
        
        if (contentType.contains("image")) {
            return "IMAGE";
        }
        return "UNKNOWN";
    }

    // TODO: Przenieść na zewnątrz
    public String getHumanReadableFileSize() {
        return FileUtils.byteCountToDisplaySize(getFileSize());
    }

    // TODO: Przenieść na zewnątrz
    void calculateAndSetHash() {
        this.hashedFileName = 
            DigestUtils.shaHex(originalFileName + contentType + 
                               ticket.getTicketId() + System.currentTimeMillis());
    }
}
