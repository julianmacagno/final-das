package com.das.rescueapp.core.config.constant;

public class Routes {
    public static class Api {
        public static class Authentication {
            public static final String REGISTER = "/authentication/register";
            public static final String LOGIN = "/authentication/login";
            public static final String REFRESH_AUTHORIZATION = "/authentication/refresh-authorization";
            public static final String VALIDATE_USER = "/authentication/validate-user";
            public static final String VALIDATE_USER_CUIL_PARAM = VALIDATE_USER + "/{cuil}";
        }

        public static class Entity {
            public static final String ENTITY = "/entity";
        }

        public static class Ping {
            public static final String PING = "/ping";
            public static final String SECURED_PING = "/secured-ping";
        }

        public static class Assistance {
            public static final String ASSISTANCE = "/assistance";
            public static final String ASSISTANCE_BY_USER_ID = ASSISTANCE + "/user/{userId}";
            public static final String ASSISTANCE_BY_USER_ID_BY_ENTITY_ID = ASSISTANCE + "/{userId}/{entityId}";
        }

        public static class Message {
            public static final String SEND_MESSAGE = "/message";
            public static final String GET_MESSAGE = "/message/{assistanceId}";
        }

        public static class Announcement {
            public static final String ANNOUNCEMENT = "/announcement";
        }

        public static class Status {
            public static final String STATUS_FOR_USER = "/status/user/{userId}";
        }

        public static class Reason {
            public static final String REASON = "/reason/{entityId}";
        }
    }
}
