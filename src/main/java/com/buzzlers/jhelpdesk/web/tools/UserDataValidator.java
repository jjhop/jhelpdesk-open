package com.buzzlers.jhelpdesk.web.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import com.buzzlers.jhelpdesk.model.User;

@Component
@Qualifier("onlyData")
public class UserDataValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    public void validate(Object user, Errors errors) {
        rejectIfEmptyOrWhitespace(errors, "firstName", "errors.user.firstName");
        rejectIfEmptyOrWhitespace(errors, "lastName", "errors.user.lastName");
        rejectIfEmptyOrWhitespace(errors, "email", "errors.user.email");

        User u = (User) user;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = u.getEmail();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (errors.getFieldErrorCount("email") == 0 && !matcher.matches()) {
            errors.rejectValue("email", "errors.user.email");
        }
    }
}

