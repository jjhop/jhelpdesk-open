package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.Article;

@Component
public class ArticleValidator implements Validator {
    
    public boolean supports(Class<?> clazz) {
        return Article.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        Article article = (Article) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "errors.kbase.articleTitle.empty");
        if (article.getTitle() != null && article.getTitle().length() > 255) {
            errors.rejectValue("title", "errors.kbase.articleTitle.toolong");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lead", "errors.kbase.articleLead.empty");
        if (article.getLead() != null && article.getLead().length() > 4096) {
            errors.rejectValue("lead", "errors.kbase.articleLead.toolong");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "body", "errors.kbase.articleBody.empty");
        if (article.getBody() != null && article.getBody().length() > 16384) {
            errors.rejectValue("body", "errors.kbase.articleBody.toolong");
        }

        if (article.getCategory() == null) {
            errors.rejectValue("category", "errors.kbase.articleCategoryNull");
        }
    }
}
