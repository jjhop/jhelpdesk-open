package com.buzzlers.jhelpdesk.model;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public enum EventType {

    CREATE(1, "eventType.create"),                  // page_add
    ASSIGN(2, "eventType.assign"),                  // page_go
    REASSIGN(3, "eventType.reassign"),              // page_refresh
    CLOSE(4, "eventType.close"),                    // page_green
    REJECT(5, "eventType.reject"),                  // page_delete
    CATEGORYCHANGE(6, "eventType.categoryChange"),  // page_code
    PRIORITYCHANGE(7, "eventType.priorityChange"),  // page_lightning
    STATUSCHANGE(8, "eventType.statusChange"),      // page_gear
    RESOLVE(11, "eventType.resolve"),               // ???
    REOPEN(12, "eventType.resolve"),                // ???
    COMMENTADD(9, "eventType.commentAdd"),          // page_paintbrush
    ATTACHMENTADD(10, "eventType.attachmentAdd");   // page_attach

    private final int code;
    private final String eventNameCode;

    EventType(int code, String eventNameCode) {
        this.code = code;
        this.eventNameCode = eventNameCode;
    }

    public int toInt() {
        return code;
    }

    public String getTypeName(Locale locale) {
        ResourceBundle names = ResourceBundle.getBundle("eventType", locale);
        return names.getString(eventNameCode);
    }
    
    public static EventType fromInt(int code) {
        return Stream.of(values())
                .filter(et -> et.code == code)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown ticketEvent type."));
    }
}
