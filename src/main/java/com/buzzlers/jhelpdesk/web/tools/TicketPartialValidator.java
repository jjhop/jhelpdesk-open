package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.buzzlers.jhelpdesk.model.Ticket;

@Component("ticketPartialValidator")
public class TicketPartialValidator {
    
    public void validateUser(Ticket ticket, String notifier, Errors errors) {
        if (ticket.getNotifier() == null) {
            if ("".equalsIgnoreCase(notifier)) {
                errors.rejectValue("notifier", "ticket.notifier.notEmpty");
            } else {
                errors.rejectValue("notifier", "ticket.notifier.notExists",
                        new Object[]{notifier}, null);
            }
        }
    }

    public void validateSubjectAndDescription(Ticket ticket, Errors errors) {
        if (ticket.getSubject() == null || ticket.getSubject().trim().length() < 3) {
            errors.rejectValue("subject", "ticket.subject.empty");
        }
        if (ticket.getDescription() == null || ticket.getDescription().trim().length() < 1) {
            errors.rejectValue("description", "ticket.description.empty");
        }
    }
}
