package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.TicketEvent;

@Repository
public interface TicketEventRepository extends JpaRepository<TicketEvent, Long> {
}
