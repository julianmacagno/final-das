package com.das.entities.commons.exceptions;

public class PortalError {
    /*** AUTHENTICATION ***/
    public static final String USER_NOT_VALIDATED = "SERVER_ERROR.USER_NOT_VALIDATED";
    public static final String USER_NOT_AUTHORIZED = "SERVER_ERROR.USER_NOT_AUTHORIZED";

    /*** EMAIL ***/
    public static final String ERROR_SENDING_REGISTRATION_EMAIL = "SERVER_ERROR.ERROR_SENDING_REGISTRATION_EMAIL";

    /*** USER ***/
    public static final String ERROR_INSERTING_USER = "SERVER_ERROR.ERROR_INSERTING_USER";
    public static final String USER_NOT_FOUND = "SERVER_ERROR.USER_NOT_FOUND";

    /*** GLOBAL ***/
    public static final String INTERNAL_SERVER_ERROR = "SERVER_ERROR.SERVER_ERROR";

    /*** ENTITY ***/
    public static final String ENTITY_NOT_FOUND = "SERVER_ERROR.ENTITY_NOT_FOUND";
}
