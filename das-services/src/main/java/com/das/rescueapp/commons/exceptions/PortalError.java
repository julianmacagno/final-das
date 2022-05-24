package com.das.rescueapp.commons.exceptions;

public class PortalError {
    /*** AUTHENTICATION ***/
    public static final String USER_NOT_VALIDATED = "SERVER_ERROR.USER_NOT_VALIDATED";
    public static final String USER_NOT_AUTHORIZED = "SERVER_ERROR.USER_NOT_AUTHORIZED";

    /*** EMAIL ***/
    public static final String ERROR_SENDING_REGISTRATION_EMAIL = "SERVER_ERROR.ERROR_SENDING_REGISTRATION_EMAIL";

    /*** USER ***/
    public static final String ERROR_INSERTING_USER = "SERVER_ERROR.ERROR_INSERTING_USER";
    public static final String USER_NOT_FOUND = "SERVER_ERROR.USER_NOT_FOUND";
    public static final String USER_LOCKED = "SERVER_ERROR.USER_LOCKED";

    /*** GLOBAL ***/
    public static final String INTERNAL_SERVER_ERROR = "SERVER_ERROR.SERVER_ERROR";

    /*** ENTITY ***/
    public static final String ENTITY_NOT_FOUND = "SERVER_ERROR.ENTITY_NOT_FOUND";

    /*** ASSISTANCE ***/
    public static final String ERROR_INSERTING_ASSISTANCE = "SERVER_ERROR.ERROR_INSERTING_ASSISTANCE";
    public static final String ASSISTANCE_NOT_FOUND = "SERVER_ERROR.ASSISTANCE_NOT_FOUND";
    public static final String CANNOT_CANCEL_ASSISTANCE = "SERVER_ERROR.CANNOT_CANCEL_ASSISTANCE";
    public static final String CANNOT_FINALIZE_ASSISTANCE = "SERVER_ERROR.CANNOT_FINALIZE_ASSISTANCE";
    public static final String ASSISTANCE_STATUS_LIST_NOT_FOUND = "SERVER_ERROR.ASSISTANCE_STATUS_LIST_NOT_FOUND";
    public static final String ERROR_GETTING_ASSISTANCE_STATUS_LIST = "SERVER_ERROR.ERROR_GETTING_ASSISTANCE_STATUS_LIST";

    /*** MESSAGE ***/
    public static final String ERROR_INSERTING_MESSAGE = "SERVER_ERROR.ERROR_INSERTING_MESSAGE";
    public static final String MESSAGE_NOT_FOUND = "SERVER_ERROR.MESSAGE_NOT_FOUND";

    /*** ANNOUNCEMENT ***/
    public static final String ERROR_GETTING_ANNOUNCEMENT = "SERVER_ERROR.ERROR_GETTING_ANNOUNCEMENT";

    /*** ASSISTANCE_STATUS ***/
    public static final String ERROR_GETTING_ASSISTANCE_STATUS = "SERVER_ERROR.ERROR_GETTING_ASSISTANCE_STATUS";

    /*** STATS ***/
    public static final String ERROR_GETTING_STATS = "SERVER_ERROR.ERROR_GETTING_STATS";

    /*** STATS ***/
    public static final String ENTITY_UNAVAILABLE = "SERVER_ERROR.ENTITY_UNAVAILABLE";
    public static final String COULD_NOT_RETRIEVE_REASON = "SERVER_ERROR.COULD_NOT_RETRIEVE_REASON";
}
