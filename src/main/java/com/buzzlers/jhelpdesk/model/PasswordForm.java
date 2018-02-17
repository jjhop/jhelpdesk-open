package com.buzzlers.jhelpdesk.model;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;

@Getter
@Setter
public class PasswordForm {

    private String currentPassword;
    private String newPassword;
    private String newPasswordRepeated;

    public boolean newPasswordValid() {
        boolean containsNumber = StringUtils.containsAny(newPassword, "0123456789");
        return newPassword.length() >= 6
                && newPassword.equals(newPasswordRepeated)
                && containsNumber;
    }
}
