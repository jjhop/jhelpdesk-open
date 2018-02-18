package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.ArticleCategory;

@Component
public class ArticleCategoryValidator implements Validator {
	
	public boolean supports(Class<?> clazz) {
		return ArticleCategory.class.equals(clazz);
	}

	public void validate(Object command, Errors errors) {
        ArticleCategory category = (ArticleCategory) command;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryName", "errors.kbase.categoryName");
        if (category.getCategoryName() != null && category.getCategoryName().length() > 128) {
             errors.rejectValue("categoryName", "errors.kbase.categoryName.toolong");
        }
	}
}
