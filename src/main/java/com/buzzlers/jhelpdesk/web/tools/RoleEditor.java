package com.buzzlers.jhelpdesk.web.tools;

import java.beans.PropertyEditorSupport;

import org.springframework.stereotype.Component;

import com.buzzlers.jhelpdesk.model.Role;

@Component
public class RoleEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        if (getValue() != null) {
            return String.valueOf(((Role) getValue()).toInt());
        } else {
            return null;
        }
    }

    @Override
    public void setAsText(String text) {
        setValue(Role.fromInt(Integer.parseInt(text)));
    }
}
