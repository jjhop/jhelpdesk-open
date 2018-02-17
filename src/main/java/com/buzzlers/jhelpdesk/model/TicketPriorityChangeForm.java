package com.buzzlers.jhelpdesk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketPriorityChangeForm {
    private TicketPriority priority;
    private String commentText;
}
