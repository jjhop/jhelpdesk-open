package com.buzzlers.jhelpdesk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketCategoryChangeForm {
    private TicketCategory category;
    private String commentText;
}
