package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.ArticleComment;

@Component
public class ArticleCommentValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return ArticleComment.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, "title", "errors.kbase.articleCommentTitle");
        rejectIfEmptyOrWhitespace(errors, "body", "errors.kbase.articleCommentBody");

        // TODO: reject too long values
    }
}
