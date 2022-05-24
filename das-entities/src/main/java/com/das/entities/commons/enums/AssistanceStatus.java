package com.das.entities.commons.enums;

public enum AssistanceStatus {
    IN_PROGRESS("in_progress"),
    FINALIZED("finalized"),
    CANCELED("canceled");

    private final String value;

    AssistanceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
