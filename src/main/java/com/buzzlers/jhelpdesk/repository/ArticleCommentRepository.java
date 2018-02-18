package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.ArticleComment;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

}
