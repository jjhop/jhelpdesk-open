package com.buzzlers.jhelpdesk.web.tools;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buzzlers.jhelpdesk.dao.UserDAO;
import com.buzzlers.jhelpdesk.model.User;

@Component
public class UserEditor extends PropertyEditorSupport {

    @Autowired
    private UserDAO userDAO;

    @Override
    public String getAsText() {
        if (getValue() != null) {
            return ((User) getValue()).getEmail();
        } else {
            return null;
        }
    }

    @Override
    public void setAsText(String text) {
        try {
            User user = userDAO.getByEmail(text);
            setValue(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
