package com.das.entities.commons.exceptions;

import com.das.entities.commons.enums.IssueType;

public class EntitiesException extends RuntimeException {
    private final IssueType issueType;
    private final String message;

    public EntitiesException(IssueType issueType, String message) {
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
