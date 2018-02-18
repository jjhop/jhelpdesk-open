package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.TicketCategory;


@Component
public class TicketCategoryValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return TicketCategory.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        TicketCategory category = (TicketCategory) target;
        rejectIfEmptyOrWhitespace(errors, "categoryName", "errors.category.categoryName");
        if (category.getCategoryName() != null && category.getCategoryName().length() > 64) {
            errors.rejectValue("categoryName", "errors.category.categoryName.toolong");
        }
        rejectIfEmptyOrWhitespace(errors, "categoryDesc", "errors.category.categoryDesc");
        if (category.getCategoryDesc() != null && category.getCategoryDesc().length() > 255) {
            errors.rejectValue("categoryDesc", "errors.category.categoryDesc.toolong");
        }
    }
}
