package com.buzzlers.jhelpdesk.model;

import java.util.stream.Stream;

public enum CommentType {
    NORMAL(0),
    REJECT(1),
    RESOLVE(2),
    REOPEN(3),
    CLOSE(4),
    CATEGORY_CHANGE(5),
    PRIORITY_CHANGE(6);

    private final int id;

    CommentType(int id) {
        this.id = id;
    }

    public int toInt() {
        return id;
    }

    public static CommentType fromInt(int id) {
        return Stream.of(values())
                .filter(ct -> ct.id == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknowny TicketComment.CommentType"));
    }
}