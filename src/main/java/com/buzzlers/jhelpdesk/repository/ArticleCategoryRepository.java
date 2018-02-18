package com.buzzlers.jhelpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buzzlers.jhelpdesk.model.ArticleCategory;

@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {
}
