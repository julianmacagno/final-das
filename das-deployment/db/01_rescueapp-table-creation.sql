USE das_rescue_app;

DROP TABLE IF EXISTS [message];
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS announcement;
DROP TABLE IF EXISTS entity;
DROP TABLE IF EXISTS [user];

CREATE TABLE [user]
(
    id        INT          NOT NULL PRIMARY KEY IDENTITY,
    cuil      VARCHAR(255) NOT NULL UNIQUE,
    name      VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    validated BIT DEFAULT 0,
    canceled  BIT DEFAULT 0
);

CREATE TABLE entity
(
    id               INT          NOT NULL PRIMARY KEY IDENTITY,
    phone            VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    available        BIT          NOT NULL,
    url              VARCHAR(255) NOT NULL,
    emergencyService BIT          NOT NULL,
    apiType          VARCHAR(255) NOT NULL,
    username         VARCHAR(255) NOT NULL,
    password         VARCHAR(255) NOT NULL
);

CREATE TABLE announcement
(
    id        INT      NOT NULL PRIMARY KEY IDENTITY,
    content   VARCHAR(MAX),
    author    VARCHAR(255),
    title     VARCHAR(255),
    timestamp DATETIME NOT NULL DEFAULT GETDATE()
)

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    id                   INT      NOT NULL PRIMARY KEY IDENTITY,
    entity_id            INT      NOT NULL FOREIGN KEY REFERENCES entity (id),
    user_id              INT      NOT NULL FOREIGN KEY REFERENCES [user] (id),
    assistance_status_id INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    timestamp            DATETIME NOT NULL DEFAULT GETDATE()
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE [message]
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL, -- backend should enforce lat long for example, if not needed empty json is ok
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);
