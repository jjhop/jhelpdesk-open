package com.buzzlers.jhelpdesk.model;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// maybe we should implement some sort of state machine here?
public enum TicketStatus {

    NOTIFIED(1, "ticketStatus.notified", "desc", "FF4040", true), // lightbulb ZGŁOSZONY
    ASSIGNED(2, "ticketStatus.assigned", "desc", "FFC843", true), // cog PRZYPISANY
    REJECTED(3, "ticketStatus.rejected", "desc", "4D61A9", true), // exclamation ODRZUCONY
    RESOLVED(4, "ticketStatus.resolved", "desc", "228664", true), // tick ROZWIĄZANY
    CLOSED(5, "ticketStatus.closed", "desc", "228664", true);     // accept ZAMKNIĘTY
    
    private final int statusId;
    private final String statusNameCode;
    private final String statusDesc;
    private final String bgColor;
    private final boolean isActive;

    private static TicketStatus[] ALL_STATUSES = {
        NOTIFIED,
        ASSIGNED,
        REJECTED,
        RESOLVED,
        CLOSED
    };

    TicketStatus(int id, String statusNameCode, String desc, String bgColor, boolean active) {
        this.isActive = active;
        this.statusDesc = desc;
        this.statusId = id;
        this.statusNameCode = statusNameCode;
        this.bgColor = bgColor;
    }

    public int toInt() {
        return statusId;
    }

    public static TicketStatus fromInt(int code) {
        switch (code) {
            case 1:
                return NOTIFIED;
            case 2:
                return ASSIGNED;
            case 3:
                return REJECTED;
            case 4:
                return RESOLVED;
            case 5:
                return CLOSED;
            default: throw new RuntimeException("Nieznany status.");
        }
    }

    public static List<TicketStatus> listFromString(String inputString) {
        return Stream.of(inputString.split(","))
                .mapToInt(Integer::parseInt)
                .mapToObj(TicketStatus::fromInt)
                .collect(toList());
    }

    public static String listAsString(List<TicketStatus> inputList) {
        return inputList != null
                ? inputList.stream()
                    .map(TicketStatus::toInt)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","))
                : "";
    }

    public static TicketStatus[] getAllStatuses() {
        return ALL_STATUSES;
    }

    public boolean getActive() { return isActive; }
    public int getStatusId() { return statusId; }
    public String getStatusDesc() { return statusDesc; }
    public String getBgColor() { return bgColor; }

    public String getStatusName(Locale locale) {
        ResourceBundle names = ResourceBundle.getBundle("statusName", locale);
        return names.getString(statusNameCode);
    }
}
