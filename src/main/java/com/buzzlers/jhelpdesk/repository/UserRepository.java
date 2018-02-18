package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
}
