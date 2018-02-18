package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.TicketActionForm;
import com.buzzlers.jhelpdesk.model.User;

@Component
public class TicketActionFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return TicketActionForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        TicketActionForm form = (TicketActionForm) target;
        User u = form.getUser();
        if (u == null || u.isPlain() || !u.isActive()) {
            // todo: reject!
        }
        if (form.getCommentText() == null
                || form.getCommentText().trim().isEmpty()) {
            errors.rejectValue("commentText", "actionForm.commentText.empty");
        }
    }
}
