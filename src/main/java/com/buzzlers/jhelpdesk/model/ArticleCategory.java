package com.buzzlers.jhelpdesk.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article_category")
@SequenceGenerator(name="article_category_sequence", sequenceName="article_category_id_seq")
@NamedQueries({
    @NamedQuery(name = "ArticleCategory.getAllByOrderASC", query = "SELECT a FROM ArticleCategory a ORDER BY a.order ASC")
})
@Getter
@Setter
public class ArticleCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="article_category_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name", length = 128)
    private String categoryName;

    @Column(name = "articles_count")
    private int articlesCount;

    @Column(name = "ord")
    private Long order;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE})
    private Set<Article> articles;

    public ArticleCategory() {
        this.articles = new HashSet<>();
    }
}
