package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.Preferences;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

}
