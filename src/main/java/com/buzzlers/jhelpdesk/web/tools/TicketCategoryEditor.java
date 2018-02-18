package com.buzzlers.jhelpdesk.web.tools;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.buzzlers.jhelpdesk.dao.TicketCategoryDAO;
import com.buzzlers.jhelpdesk.model.TicketCategory;

@Component
public class TicketCategoryEditor extends PropertyEditorSupport {

    @Autowired
    private TicketCategoryDAO ticketCategoryDAO;

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value != null) {
            TicketCategory tCategory = (TicketCategory) value;
            return String.valueOf(tCategory.getId());
        } else {
            return null;
        }
    }

    @Override
    public void setAsText(String text) {
        try {
            Long categoryId = Long.valueOf(text);
            TicketCategory category = ticketCategoryDAO.getById(categoryId);
            setValue(category);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
