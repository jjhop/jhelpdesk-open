package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.TicketFilter;

@Component
public class TicketFilterValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return TicketFilter.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        TicketFilter filter = (TicketFilter) target;
        rejectIfEmptyOrWhitespace(errors, "name", "ticketFilter.name.empty");
        if (filter.getName() != null && filter.getName().length() > 32) {
            errors.rejectValue("name", "ticketFilter.name.toolong");
        }
        if (filter.getDescription() != null && filter.getDescription().length() > 512) {
            errors.rejectValue("description", "ticketFilter.description.toolong");
        }
    }
}
