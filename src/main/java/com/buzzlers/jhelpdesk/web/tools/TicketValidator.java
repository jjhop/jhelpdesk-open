package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.Ticket;

@Component("ticketValidator")
public class TicketValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return Ticket.class.equals(clazz);
    }

    public void validate(Object command, Errors errors) {
        Ticket ticketToValidate = (Ticket)command;
        validateSubject(ticketToValidate, errors);
        validateDescription(ticketToValidate, errors);
        validateNotifier(ticketToValidate, errors);
    }

    private void validateSubject(Ticket ticketToValidate, Errors errors) {
        if (ticketToValidate.getSubject() == null
                || ticketToValidate.getSubject().trim().isEmpty()) { // isEmpty jest z jdk 1.6
            errors.rejectValue("subject", "ticket.subject.error");
        }
    }

    private void validateDescription(Ticket ticketToValidate, Errors errors) {
        if (ticketToValidate.getDescription() == null
                || ticketToValidate.getDescription().trim().isEmpty()) {
            errors.rejectValue("description", "ticket.description.error");
        }
    }

    public void validateNotifier(Ticket ticketToValidate, Errors errors) {
        if (ticketToValidate.getNotifier() == null) {
            errors.rejectValue("notifier", "ticket.notifier.error");
        }
    }
}
