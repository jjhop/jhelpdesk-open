package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.TicketFilter;

@Repository
public interface TicketFilterRepository extends JpaRepository<TicketFilter, Long> {
}
