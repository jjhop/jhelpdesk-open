package com.buzzlers.jhelpdesk.web.tools;

import java.beans.PropertyEditorSupport;

import org.springframework.stereotype.Component;

import com.buzzlers.jhelpdesk.model.TicketPriority;

@Component
public class TicketPriorityEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        if (getValue() != null) {
            return String.valueOf(((TicketPriority)getValue()).toInt());
        } else {
            return null;
        }
    }

    @Override
    public void setAsText(String text) {
        int ticketPriorityId = Integer.valueOf(text);
        setValue(TicketPriority.fromInt(ticketPriorityId));
    }
}
