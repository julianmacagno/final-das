package com.das.entities.core.config.constant;

public class Routes {
    public static class Rest {
        public static class Authentication {
            public static final String LOGIN = "/rest/authentication/login";
            public static final String REFRESH_AUTHORIZATION = "/rest/authentication/refresh-authorization";
        }

        public static class Ping {
            public static final String PING = "/rest/ping";
            public static final String SECURED_PING = "/rest/secured-ping";
        }

        public static class Availability {
            public static final String AVAILABILITY = "/rest/availability";
        }

        public static class Message {
            public static final String message = "/rest/message";
        }

        public static class Reason {
            public static final String reason = "/rest/reason";
        }
    }

    public static class Soap {
        public static final String NAMESPACE_URI = "http://das.com/soap";
        public static final String SOAP = "/soap";
    }

    public static class SoapAuth {
        public static final String NAMESPACE_URI = "http://das.com/soap_auth";
        public static final String SOAP_AUTH = "/soap_auth";
    }
}
