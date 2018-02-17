package com.buzzlers.jhelpdesk.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketActionForm {
    private User user;
    private String commentText;
}
