package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.Announcement;

@Component
public class AnnouncementValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return Announcement.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        Announcement announcement = (Announcement) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "errors.announcement.title");
        if (announcement.getTitle() != null & announcement.getTitle().length() > 255) {
            errors.rejectValue("title", "errors.announcement.title.toolong");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lead", "errors.announcement.lead");
        if (announcement.getLead() != null & announcement.getLead().length() > 4096) {
            errors.rejectValue("lead", "errors.announcement.lead.toolong"); // za długie
        }

        if (announcement.getBody() != null && announcement.getBody().length() > 16384) {
            errors.rejectValue("body", "errors.announcement.body.toolong"); // za długie
        }
    }
}
