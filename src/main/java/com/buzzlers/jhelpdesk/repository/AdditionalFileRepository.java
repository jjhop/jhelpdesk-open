package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.AdditionalFile;

@Repository
public interface AdditionalFileRepository extends JpaRepository<AdditionalFile, Long> {
}
