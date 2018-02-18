package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.apache.commons.lang.StringUtils.isBlank;

import com.buzzlers.jhelpdesk.model.TicketPriorityChangeForm;

@Component
public class TicketPriorityChangeFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return TicketPriorityChangeForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        TicketPriorityChangeForm form = (TicketPriorityChangeForm) target;
        if (isBlank(form.getCommentText())) {
            errors.rejectValue("commentText", "ticketCategoryChangeForm.commentText.empty");
        }
    }
}
