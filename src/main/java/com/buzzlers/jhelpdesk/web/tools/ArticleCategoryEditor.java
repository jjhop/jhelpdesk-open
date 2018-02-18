package com.buzzlers.jhelpdesk.web.tools;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buzzlers.jhelpdesk.dao.ArticleCategoryDAO;
import com.buzzlers.jhelpdesk.model.ArticleCategory;

@Component
public class ArticleCategoryEditor extends PropertyEditorSupport {

    @Autowired
    private ArticleCategoryDAO articleCategoryDAO;

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value != null) {
            return String.valueOf(((ArticleCategory) value).getId());
        } else {
            return null;
        }
    }

    @Override
    public void setAsText(String text) {
        try {
            Long articleCategoryId = Long.valueOf(text);
            ArticleCategory articleCategory = articleCategoryDAO.getById(articleCategoryId);
            setValue(articleCategory);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
