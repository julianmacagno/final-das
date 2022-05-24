USE das_rescue_app;
GO;

CREATE OR ALTER PROCEDURE GetUserById(
    @userId INT
)
AS
BEGIN
    SELECT id,
           cuil,
           name,
           email,
           password,
           validated,
           canceled
    FROM [user]
    WHERE id = @userId
END;
GO;

CREATE OR ALTER PROCEDURE LoginUser(
    @cuil VARCHAR(255),
    @password VARCHAR(255)
)
AS
BEGIN
    SELECT id,
           cuil,
           name,
           validated,
           canceled
    FROM [user]
    WHERE (@cuil IS NOT NULL AND cuil = @cuil)
      AND (@password IS NOT NULL AND password = @password COLLATE Latin1_General_CS_AS)
END;
GO;

CREATE OR ALTER PROCEDURE InsertUser(
    @cuil VARCHAR(255),
    @name VARCHAR(255),
    @email VARCHAR(255),
    @password VARCHAR(255)
)
AS
BEGIN
    INSERT INTO [user](cuil, name, email, password)
    VALUES (@cuil, @name, @email, @password);
END;
GO;

CREATE OR ALTER PROCEDURE ValidateUser(
    @cuil VARCHAR(255)
)
AS
BEGIN
    UPDATE [user]
    SET validated = 1
    WHERE CONVERT(VARCHAR(MAX), HASHBYTES('SHA2_256', cuil), 2) = @cuil;
END;
GO;

CREATE OR ALTER PROCEDURE GetEntity(
    @available BIT,
    @isEmergency BIT,
    @entityId INT
)
AS
BEGIN
    SELECT *
    FROM entity
    WHERE (@isEmergency IS NULL OR emergencyService = @isEmergency)
      AND (@available IS NULL OR available = @available)
      AND (@entityId IS NULL OR id = @entityId);
END;
GO;

CREATE OR ALTER PROCEDURE UpdateEntity(
    @id INT,
    @available BIT
)
AS
BEGIN
    UPDATE entity
    SET available = @available
    WHERE id = @id
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @entityId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(entity_id, user_id, assistance_status_id)
    SELECT @entityId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus

    SELECT SCOPE_IDENTITY() AS id;
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @id INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.id = @id

    SELECT SCOPE_IDENTITY() AS id;
END;
GO;

CREATE OR ALTER PROCEDURE GetAssistanceStatusId(
    @assistanceStatus INT
)
AS
BEGIN
    SELECT [as].id AS assistance_status_id
    FROM assistance a
             JOIN assistance_status [as] ON a.assistance_status_id = [as].id
    WHERE [as].name = @assistanceStatus;
END;
GO;

CREATE OR ALTER PROCEDURE GetAssistance(
    @entityId INT,
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    SELECT a.id
    FROM assistance a
             JOIN assistance_status [as] ON a.assistance_status_id = [as].id
    WHERE [as].name = @assistanceStatus
      AND a.entity_id = @entityId
      AND a.user_id = @userId
    ORDER BY timestamp DESC;
END;
GO;


CREATE OR ALTER PROCEDURE InsertMessage(
    @assistanceId INT,
    @messageType VARCHAR(255),
    @payload VARCHAR(1024),
    @attachment VARCHAR(MAX),
    @synchronized BIT,
    @isFromUser BIT
)
AS
BEGIN
    INSERT INTO message(assistance_id, message_type_id, payload, attachment, synchronized, is_from_user)
    SELECT @assistanceId, mt.id, @payload, @attachment, @synchronized, @isFromUser
    FROM message_type mt
    WHERE mt.name = @messageType
END;
GO;

CREATE OR ALTER PROCEDURE SelectMessage(
    @assistanceId INT
)
AS
BEGIN
    SELECT m.id           AS id,
           a.id           AS assistanceId,
           a.user_id      AS userId,
           a.entity_id    AS entityId,
           m.payload      AS payload,
           m.attachment   AS attachment,
           mt.name        AS messageType,
           m.timestamp    AS timestamp,
           m.is_from_user AS isFromUser
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt ON m.message_type_id = mt.id
    WHERE m.assistance_id = @assistanceId
    ORDER BY m.timestamp ASC, m.id ASC;
END;
GO;

CREATE OR ALTER PROCEDURE GetNotSynchronizedMessages
AS
BEGIN
    SELECT m.id,
           m.assistance_id AS assistanceId,
           a.user_id       AS userId,
           a.entity_id     AS entityId,
           m.payload,
           m.attachment,
           mt.name         AS messageType,
           m.timestamp,
           m.is_from_user  AS isFromUser,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
             JOIN entity e
                  ON a.entity_id = e.id
    WHERE m.synchronized = 0;
END;
GO;

CREATE OR ALTER PROCEDURE MarkAsSynchronized(
    @entityId INT
)
AS
BEGIN
    UPDATE m
    SET synchronized = 1
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
    WHERE a.entity_id = @entityId;
END;
GO;

CREATE OR ALTER PROCEDURE GetAnnouncement
AS
BEGIN
    SELECT a.content
    FROM announcement a
    ORDER BY timestamp DESC
END;
GO;

CREATE OR ALTER PROCEDURE GetActiveAssistanceForUser(
    @userId INT
)
AS
BEGIN
    SELECT TOP 1 WITH TIES a.entity_id,
                           a.timestamp,
                           a.entity_id,
                           [as].name AS assistance_status,
                           e.name    AS entity_name,
                           a.id,
                           a.user_id
    FROM assistance a
             JOIN assistance_status [as] ON a.assistance_status_id = [as].id
             JOIN entity e ON a.entity_id = e.id
    WHERE user_id = @userId
    ORDER BY ROW_NUMBER() OVER (PARTITION BY entity_id ORDER BY [timestamp] DESC);
END
GO;

CREATE OR ALTER PROCEDURE GetAssistanceListForUser(
    @userId INT
)
AS
BEGIN
    SELECT a.id, a.timestamp, e.name AS entity_name, [as].name AS assistance_status
    FROM assistance a
             JOIN entity e ON a.entity_id = e.id
             JOIN [user] u ON a.user_id = u.id
             JOIN assistance_status [as] ON a.assistance_status_id = [as].id
    WHERE u.id = @userId
    ORDER BY a.timestamp DESC;
END;
GO;

CREATE OR ALTER PROCEDURE GetStats
AS
BEGIN
    SELECT (SELECT COUNT(*)
            FROM assistance a
                     JOIN assistance_status [as] ON a.assistance_status_id = [as].id
            WHERE [as].name = 'in_progress'
              AND user_id = u.id) open_assistance_count,
           (SELECT COUNT(*)
            FROM assistance a
                     JOIN assistance_status [as] ON a.assistance_status_id = [as].id
            WHERE [as].name = 'finalized'
              AND user_id = u.id) finished_assistance_count,
           (SELECT COUNT(*)
            FROM assistance a
                     JOIN assistance_status [as] ON a.assistance_status_id = [as].id
            WHERE [as].name = 'canceled'
              AND user_id = u.id) canceled_assistance_count,
           (SELECT COUNT(*)
            FROM message m2
                     JOIN assistance a2 ON a2.id = m2.assistance_id
            WHERE user_id = u.id) message_count,
           id AS                  user_id
    FROM [user] u
END