package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.TicketComment;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
}
