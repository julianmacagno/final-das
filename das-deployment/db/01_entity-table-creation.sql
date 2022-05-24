USE das_entity;

DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS reason;
DROP TABLE IF EXISTS assistance_valoration_scale;

CREATE TABLE reason
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name varchar(255) NOT NULL
);

CREATE TABLE assistance_valoration_scale
(
    id       INT NOT NULL PRIMARY KEY IDENTITY,
    value    INT NOT NULL,
    positive BIT NOT NULL
);

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    timestamp              DATETIME NOT NULL DEFAULT GETDATE()
    id                     INT      NOT NULL PRIMARY KEY,
    user_id                INT      NOT NULL,
    assistance_status_id   INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    cancellation_reason_id INT FOREIGN KEY REFERENCES reason (id),
    timestamp            DATETIME NOT NULL DEFAULT GETDATE(),
    valoration           INT FOREIGN KEY REFERENCES assistance_valoration_scale (id)
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE message
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL,
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);

CREATE TABLE stat
(
    id                        INT      NOT NULL PRIMARY KEY IDENTITY,
    userId                    INT      NOT NULL,
    batch_start               DATETIME NOT NULL,
    batch_end                 DATETIME NOT NULL,
    open_assistance_count     INT      NOT NULL,
    finished_assistance_count INT      NOT NULL,
    canceled_assistance_count INT      NOT NULL,
    message_count             INT      NOT NULL
);

USE das_firedept;

DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS reason;
DROP TABLE IF EXISTS assistance_valoration_scale;

CREATE TABLE reason
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name varchar(255) NOT NULL
);

CREATE TABLE assistance_valoration_scale
(
    id       INT NOT NULL PRIMARY KEY IDENTITY,
    value    INT NOT NULL,
    positive BIT NOT NULL
);

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    id                     INT      NOT NULL PRIMARY KEY,
    user_id                INT      NOT NULL,
    assistance_status_id   INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    cancellation_reason_id INT FOREIGN KEY REFERENCES reason (id),
    timestamp            DATETIME NOT NULL DEFAULT GETDATE(),
    valoration           INT FOREIGN KEY REFERENCES assistance_valoration_scale (id)
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE message
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL,
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);

CREATE TABLE stat
(
    id                        INT      NOT NULL PRIMARY KEY IDENTITY,
    userId                    INT      NOT NULL,
    batch_start               DATETIME NOT NULL,
    batch_end                 DATETIME NOT NULL,
    open_assistance_count     INT      NOT NULL,
    finished_assistance_count INT      NOT NULL,
    canceled_assistance_count INT      NOT NULL,
    message_count             INT      NOT NULL
);

USE das_police;

DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS reason;
DROP TABLE IF EXISTS assistance_valoration_scale;

CREATE TABLE reason
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name varchar(255) NOT NULL
);

CREATE TABLE assistance_valoration_scale
(
    id       INT NOT NULL PRIMARY KEY IDENTITY,
    value    INT NOT NULL,
    positive BIT NOT NULL
);

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    id                     INT      NOT NULL PRIMARY KEY,
    user_id                INT      NOT NULL,
    assistance_status_id   INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    cancellation_reason_id INT FOREIGN KEY REFERENCES reason (id),
    timestamp            DATETIME NOT NULL DEFAULT GETDATE(),
    valoration           INT FOREIGN KEY REFERENCES assistance_valoration_scale (id)
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE message
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL,
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);

CREATE TABLE stat
(
    id                        INT      NOT NULL PRIMARY KEY IDENTITY,
    userId                    INT      NOT NULL,
    batch_start               DATETIME NOT NULL,
    batch_end                 DATETIME NOT NULL,
    open_assistance_count     INT      NOT NULL,
    finished_assistance_count INT      NOT NULL,
    canceled_assistance_count INT      NOT NULL,
    message_count             INT      NOT NULL
);

USE das_ambulance;

DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS reason;
DROP TABLE IF EXISTS assistance_valoration_scale;

CREATE TABLE reason
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name varchar(255) NOT NULL
);

CREATE TABLE assistance_valoration_scale
(
    id       INT NOT NULL PRIMARY KEY IDENTITY,
    value    INT NOT NULL,
    positive BIT NOT NULL
);

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    id                     INT      NOT NULL PRIMARY KEY,
    user_id                INT      NOT NULL,
    assistance_status_id   INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    cancellation_reason_id INT FOREIGN KEY REFERENCES reason (id),
    timestamp            DATETIME NOT NULL DEFAULT GETDATE(),
    valoration           INT FOREIGN KEY REFERENCES assistance_valoration_scale (id)
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE message
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL,
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);

CREATE TABLE stat
(
    id                        INT      NOT NULL PRIMARY KEY IDENTITY,
    userId                    INT      NOT NULL,
    batch_start               DATETIME NOT NULL,
    batch_end                 DATETIME NOT NULL,
    open_assistance_count     INT      NOT NULL,
    finished_assistance_count INT      NOT NULL,
    canceled_assistance_count INT      NOT NULL,
    message_count             INT      NOT NULL
);

USE das_civildef;

DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS reason;
DROP TABLE IF EXISTS assistance_valoration_scale;

CREATE TABLE reason
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name varchar(255) NOT NULL
);

CREATE TABLE assistance_valoration_scale
(
    id       INT NOT NULL PRIMARY KEY IDENTITY,
    value    INT NOT NULL,
    positive BIT NOT NULL
);

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    id                     INT      NOT NULL PRIMARY KEY,
    user_id                INT      NOT NULL,
    assistance_status_id   INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    cancellation_reason_id INT FOREIGN KEY REFERENCES reason (id),
    valoration           INT FOREIGN KEY REFERENCES assistance_valoration_scale (id)
    timestamp            DATETIME NOT NULL DEFAULT GETDATE(),
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE message
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL,
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);

CREATE TABLE stat
(
    id                        INT      NOT NULL PRIMARY KEY IDENTITY,
    userId                    INT      NOT NULL,
    batch_start               DATETIME NOT NULL,
    batch_end                 DATETIME NOT NULL,
    open_assistance_count     INT      NOT NULL,
    finished_assistance_count INT      NOT NULL,
    canceled_assistance_count INT      NOT NULL,
    message_count             INT      NOT NULL
);

USE das_muni;

DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_type;
DROP TABLE IF EXISTS assistance;
DROP TABLE IF EXISTS assistance_status;
DROP TABLE IF EXISTS reason;
DROP TABLE IF EXISTS assistance_valoration_scale;

CREATE TABLE reason
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name varchar(255) NOT NULL
);

CREATE TABLE assistance_valoration_scale
(
    id       INT NOT NULL PRIMARY KEY IDENTITY,
    value    INT NOT NULL,
    positive BIT NOT NULL
);

CREATE TABLE assistance_status
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE assistance
(
    id                     INT      NOT NULL PRIMARY KEY,
    user_id                INT      NOT NULL,
    assistance_status_id   INT      NOT NULL FOREIGN KEY REFERENCES assistance_status (id),
    cancellation_reason_id INT FOREIGN KEY REFERENCES reason (id),
    timestamp            DATETIME NOT NULL DEFAULT GETDATE(),
    valoration           INT FOREIGN KEY REFERENCES assistance_valoration_scale (id)
);

CREATE TABLE message_type
(
    id   INT          NOT NULL PRIMARY KEY IDENTITY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE message
(
    id              INT           NOT NULL PRIMARY KEY IDENTITY,
    assistance_id   INT           NOT NULL FOREIGN KEY REFERENCES assistance (id),
    message_type_id INT           NOT NULL FOREIGN KEY REFERENCES message_type (id),
    payload         VARCHAR(1024) NOT NULL,
    attachment      VARCHAR(MAX),
    synchronized    BIT                    DEFAULT 0 NOT NULL,
    timestamp       DATETIME      NOT NULL DEFAULT GETDATE(),
    is_from_user    BIT           NOT NULL
);

CREATE TABLE stat
(
    id                        INT      NOT NULL PRIMARY KEY IDENTITY,
    userId                    INT      NOT NULL,
    batch_start               DATETIME NOT NULL,
    batch_end                 DATETIME NOT NULL,
    open_assistance_count     INT      NOT NULL,
    finished_assistance_count INT      NOT NULL,
    canceled_assistance_count INT      NOT NULL,
    message_count             INT      NOT NULL
);