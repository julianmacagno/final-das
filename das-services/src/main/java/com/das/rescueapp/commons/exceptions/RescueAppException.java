package com.das.rescueapp.commons.exceptions;

import com.das.rescueapp.commons.enums.IssueType;

public class RescueAppException extends RuntimeException {
    private final IssueType issueType;
    private final String message;

    public RescueAppException(IssueType issueType, String message) {
        super();
        this.issueType = issueType;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public IssueType getIssueType() {
        return issueType;
    }
}
