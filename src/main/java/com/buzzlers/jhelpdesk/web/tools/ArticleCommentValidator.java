package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.ArticleComment;

@Component
public class ArticleCommentValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return ArticleComment.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "errors.kbase.articleCommentTitle");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "body", "errors.kbase.articleCommentBody");

        // TODO: reject too long values
    }
}
