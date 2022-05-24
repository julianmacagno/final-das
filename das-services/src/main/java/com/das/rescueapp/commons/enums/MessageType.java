package com.das.rescueapp.commons.enums;

public enum MessageType {
    CREATION("creation"),
    MESSAGE("message"),
    FINALIZATION("finalization"),
    CANCELATION("cancelation"),
    VALORATION("valoration");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
