package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.TicketComment;

@Component
public class TicketCommentValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        TicketComment.class.equals(clazz);
        return TicketComment.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        TicketComment comment = (TicketComment) target;
        rejectIfEmptyOrWhitespace(errors, "commentText", "errors.ticket.comment.commentText");
        if (comment.getCommentText() != null && comment.getCommentText().length() > 4096) {
            errors.rejectValue("commentText", "errors.ticket.comment.commentText.toolong");
        }
    }
}
