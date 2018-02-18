package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.buzzlers.jhelpdesk.model.PasswordForm;

@Component
public class PasswordFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return PasswordForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        PasswordForm form = (PasswordForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "passw.change.new.empty");
        if (errors.hasFieldErrors("newPassword")) {
            return;
        }
        
        if (!form.newPasswordValid()) {
            errors.rejectValue("newPassword", "passw.change.new.notValid");
        }
    }

}
