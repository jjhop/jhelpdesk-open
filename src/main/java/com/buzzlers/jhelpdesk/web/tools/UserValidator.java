package com.buzzlers.jhelpdesk.web.tools;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.User;

@Component
@Qualifier("complete")
public class UserValidator extends UserDataValidator {

    @Override
    public void validate(Object user, Errors errors) {
        super.validate(user, errors);
        rejectIfEmptyOrWhitespace(errors, "password", "errors.user.password");
        if (((User) user).getUserRole() == null) {
            errors.rejectValue("userRole", "errors.user.userRole.notset");
        }
    }
}
