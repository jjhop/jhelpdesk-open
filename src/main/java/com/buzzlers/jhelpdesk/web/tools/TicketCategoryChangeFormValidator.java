package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.TicketCategoryChangeForm;

@Component
public class TicketCategoryChangeFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return TicketCategoryChangeForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, "commentText", "ticketCategoryChangeForm.commentText.empty");
    }
}
