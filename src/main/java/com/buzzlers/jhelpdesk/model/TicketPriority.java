package com.buzzlers.jhelpdesk.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

public enum TicketPriority {

    CRITICAL(1, "ticketPriority.critical"),
    MAJOR(2, "ticketPriority.major"),
    IMPORTANT(3, "ticketPriority.important"),
    NORMAL(4, "ticketPriority.normal"),
    LOW(5, "ticketPriority.low");

    private static final List<TicketPriority> ps;

    static {
        ps = new ArrayList<>();
        ps.add(LOW);
        ps.add(NORMAL);
        ps.add(IMPORTANT);
        ps.add(MAJOR);
        ps.add(CRITICAL);
    }

    private final int priorityId;

    private final String priorityNameCode;

    TicketPriority(int id, String priorityNameCode) {
        this.priorityId = id;
        this.priorityNameCode = priorityNameCode;
    }

    public String getPriorityName(Locale locale) {
        ResourceBundle names = ResourceBundle.getBundle("priorityName", locale);
        return names.getString(priorityNameCode);
    }

    public int toInt() {
        return this.priorityId;
    }

    public static TicketPriority fromInt(int id) {
        return Stream.of(values())
                .filter(tp -> tp.toInt() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ticketPriority id"));
    }

    public static List<TicketPriority> listFromString(String inputString) {
        return Stream.of(inputString.split(","))
                .mapToInt(Integer::parseInt)
                .mapToObj(TicketPriority::fromInt)
                .collect(toList());
    }

    public static String listAsString(List<TicketPriority> inputList) {
        return inputList != null
                ? inputList.stream()
                    .map(TicketPriority::toInt)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","))
                : "";
    }

    public static List<TicketPriority> getPriorities() {
        return Collections.unmodifiableList(ps);
    }
}
