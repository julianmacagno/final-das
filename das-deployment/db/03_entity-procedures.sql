USE das_entity;
GO;

CREATE OR ALTER PROCEDURE setValoration(
    @assistanceId INT,
    @json VARCHAR(MAX)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = JSON_VALUE(@json, '$.valoration.value')
    WHERE id = @assistanceId
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId int,
    @assistanceStatus varchar(255),
    @cancellationReason varchar(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id   = (SELECT as2.id
                                             FROM assistance_status as2
                                             WHERE as2.name = @assistanceStatus),
        assistance.cancellation_reason_id = (SELECT r.id
                                             FROM reason r
                                             WHERE r.name = @cancellationReason)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE selectReason
AS
BEGIN
    SELECT id, name
    FROM reason;
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    DECLARE @json VARCHAR(MAX)

    SELECT @json = (SELECT value, positive
                    FROM assistance_valoration_scale
                    FOR JSON PATH)

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId,
            CONCAT('{"valorations":', @json, '}'),
            '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;
-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

CREATE OR ALTER PROCEDURE SetAssistanceValoration(
    @id INT, @val INT
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = @val
    WHERE assistance.id = @id
END;
GO;

USE das_police;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId, '', '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;

-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

USE das_firedept;
GO;

CREATE OR ALTER PROCEDURE setValoration(
    @assistanceId INT,
    @json VARCHAR(MAX)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = JSON_VALUE(@json, '$.valoration.value')
    WHERE id = @assistanceId
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    DECLARE @json VARCHAR(MAX)

    SELECT @json = (SELECT value, positive
                    FROM assistance_valoration_scale
                    FOR JSON PATH)

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId,
            CONCAT('{"valorations":', @json, '}'),
            '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;
-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

CREATE OR ALTER PROCEDURE SetAssistanceValoration(
    @id INT, @val INT
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = @val
    WHERE assistance.id = @id
END;
GO;

USE das_police;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId int,
    @assistanceStatus varchar(255),
    @cancellationReason varchar(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id   = (SELECT as2.id
                                             FROM assistance_status as2
                                             WHERE as2.name = @assistanceStatus),
        assistance.cancellation_reason_id = (SELECT r.id
                                             FROM reason r
                                             WHERE r.name = @cancellationReason)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE selectReason
AS
BEGIN
    SELECT id, name
    FROM reason;
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId, '', '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;

-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

USE das_police;
GO;

CREATE OR ALTER PROCEDURE setValoration(
    @assistanceId INT,
    @json VARCHAR(MAX)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = JSON_VALUE(@json, '$.valoration.value')
    WHERE id = @assistanceId
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId int,
    @assistanceStatus varchar(255),
    @cancellationReason varchar(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id   = (SELECT as2.id
                                             FROM assistance_status as2
                                             WHERE as2.name = @assistanceStatus),
        assistance.cancellation_reason_id = (SELECT r.id
                                             FROM reason r
                                             WHERE r.name = @cancellationReason)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE selectReason
AS
BEGIN
    SELECT id, name
    FROM reason;
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    DECLARE @json VARCHAR(MAX)

    SELECT @json = (SELECT value, positive
                    FROM assistance_valoration_scale
                    FOR JSON PATH)

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId,
            CONCAT('{"valorations":', @json, '}'),
            '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;
-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

CREATE OR ALTER PROCEDURE SetAssistanceValoration(
    @id INT, @val INT
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = @val
    WHERE assistance.id = @id
END;
GO;

USE das_police;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId, '', '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;

-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

USE das_ambulance;
GO;

CREATE OR ALTER PROCEDURE setValoration(
    @assistanceId INT,
    @json VARCHAR(MAX)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = JSON_VALUE(@json, '$.valoration.value')
    WHERE id = @assistanceId
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId int,
    @assistanceStatus varchar(255),
    @cancellationReason varchar(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id   = (SELECT as2.id
                                             FROM assistance_status as2
                                             WHERE as2.name = @assistanceStatus),
        assistance.cancellation_reason_id = (SELECT r.id
                                             FROM reason r
                                             WHERE r.name = @cancellationReason)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE selectReason
AS
BEGIN
    SELECT id, name
    FROM reason;
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    DECLARE @json VARCHAR(MAX)

    SELECT @json = (SELECT value, positive
                    FROM assistance_valoration_scale
                    FOR JSON PATH)

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId,
            CONCAT('{"valorations":', @json, '}'),
            '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;
-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

CREATE OR ALTER PROCEDURE SetAssistanceValoration(
    @id INT, @val INT
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = @val
    WHERE assistance.id = @id
END;
GO;

USE das_police;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId, '', '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;

-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

USE das_muni;
GO;

CREATE OR ALTER PROCEDURE setValoration(
    @assistanceId INT,
    @json VARCHAR(MAX)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = JSON_VALUE(@json, '$.valoration.value')
    WHERE id = @assistanceId
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId int,
    @assistanceStatus varchar(255),
    @cancellationReason varchar(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id   = (SELECT as2.id
                                             FROM assistance_status as2
                                             WHERE as2.name = @assistanceStatus),
        assistance.cancellation_reason_id = (SELECT r.id
                                             FROM reason r
                                             WHERE r.name = @cancellationReason)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE selectReason
AS
BEGIN
    SELECT id, name
    FROM reason;
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    DECLARE @json VARCHAR(MAX)

    SELECT @json = (SELECT value, positive
                    FROM assistance_valoration_scale
                    FOR JSON PATH)

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId,
            CONCAT('{"valorations":', @json, '}'),
            '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;
-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

CREATE OR ALTER PROCEDURE SetAssistanceValoration(
    @id INT, @val INT
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = @val
    WHERE assistance.id = @id
END;
GO;

USE das_police;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId, '', '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;

-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

USE das_civildef;
GO;

CREATE OR ALTER PROCEDURE setValoration(
    @assistanceId INT,
    @json VARCHAR(MAX)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = JSON_VALUE(@json, '$.valoration.value')
    WHERE id = @assistanceId
END;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId int,
    @assistanceStatus varchar(255),
    @cancellationReason varchar(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id   = (SELECT as2.id
                                             FROM assistance_status as2
                                             WHERE as2.name = @assistanceStatus),
        assistance.cancellation_reason_id = (SELECT r.id
                                             FROM reason r
                                             WHERE r.name = @cancellationReason)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE selectReason
AS
BEGIN
    SELECT id, name
    FROM reason;
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    DECLARE @json VARCHAR(MAX)

    SELECT @json = (SELECT value, positive
                    FROM assistance_valoration_scale
                    FOR JSON PATH)

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId,
            CONCAT('{"valorations":', @json, '}'),
            '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;
-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';

CREATE OR ALTER PROCEDURE SetAssistanceValoration(
    @id INT, @val INT
)
AS
BEGIN
    UPDATE assistance
    SET assistance.valoration = @val
    WHERE assistance.id = @id
END;
GO;

USE das_police;
GO;

CREATE OR ALTER PROCEDURE InsertAsistance(
    @assistanceId INT,
    @userId INT,
    @asistanceStatus VARCHAR(255)
)
AS
BEGIN
    INSERT INTO assistance(id, user_id, assistance_status_id)
    SELECT @assistanceId, @userId, [as].id
    FROM assistance_status [as]
    WHERE [as].name = @asistanceStatus
END;
GO;

CREATE OR ALTER PROCEDURE UpdateAssistanceStatus(
    @userId INT,
    @assistanceStatus VARCHAR(255)
)
AS
BEGIN
    UPDATE assistance
    SET assistance.assistance_status_id = (SELECT as2.id
                                           FROM assistance_status as2
                                           WHERE as2.name = @assistanceStatus)
    WHERE assistance.user_id = @userId
      AND assistance.timestamp = (SELECT TOP 1 timestamp
                                  FROM assistance
                                  WHERE user_id = @userId
                                  ORDER BY timestamp DESC)

    SELECT SCOPE_IDENTITY() AS id;
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

CREATE OR ALTER PROCEDURE MarkAsSynchronizedAndRetrieve
AS
BEGIN
    DECLARE @temp TABLE
                  (
                      id           INT,
                      assistanceId INT,
                      userId       INT,
                      payload      VARCHAR(1024),
                      attachment   VARCHAR(MAX),
                      messageType  VARCHAR(255),
                      timestamp    DATETIME,
                      isFromUser   BIT,
                      synchronized BIT
                  )

    INSERT INTO @temp
    SELECT m.id,
           m.assistance_id,
           a.user_id,
           m.payload,
           m.attachment,
           mt.name,
           m.timestamp,
           m.is_from_user,
           m.synchronized
    FROM message m
             JOIN assistance a
                  ON m.assistance_id = a.id
             JOIN message_type mt
                  ON mt.id = m.message_type_id
    WHERE m.synchronized = 0

    UPDATE message
    SET synchronized = 1
    WHERE synchronized = 0;

    SELECT *
    FROM @temp;
END;
GO;

CREATE OR ALTER PROCEDURE InsertStat(
    @userId INT,
    @batchStart DATETIME,
    @batchEnd DATETIME,
    @openAssistanceCount INT,
    @finishedAssistanceCount INT,
    @canceledAssistanceCount INT,
    @messageCount INT
)
AS
BEGIN
    INSERT INTO stat
    VALUES (@userId,
            @batchStart,
            @batchEnd,
            @openAssistanceCount,
            @finishedAssistanceCount,
            @canceledAssistanceCount,
            @messageCount)
END;
GO;

CREATE OR ALTER PROCEDURE finalizeAssistance(
    @userId INT
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @assistanceStatusFinalizedId INT = (SELECT [as].id
                                                FROM assistance_status [as]
                                                WHERE [as].name = 'finalized');
    DECLARE @messageTypeFinalizedId INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'finalization');

    UPDATE a
    SET a.assistance_status_id = @assistanceStatusFinalizedId
    FROM assistance a
    WHERE a.id = @assistanceId;

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeFinalizedId, '', '', 0, 0);
END
GO;

-- EXEC finalizeAssistance 1;

CREATE OR ALTER PROCEDURE insertResponseMessage(
    @userId INT,
    @message VARCHAR(255),
    @assistantName VARCHAR(255)
)
AS
BEGIN
    DECLARE @assistanceId INT = (SELECT TOP 1 id FROM assistance WHERE user_id = @userId ORDER BY timestamp DESC);
    DECLARE @messageTypeMessage INT = (SELECT mt.id FROM message_type mt WHERE mt.name = 'message');

    INSERT INTO message(assistance_id, message_type_id, payload, attachment, is_from_user, synchronized)
    VALUES (@assistanceId, @messageTypeMessage,
            CONCAT('{"message":"', @message, '","assistant":"', @assistantName, '"}'), '', 0, 0);
END
GO;

-- EXEC insertResponseMessage @userId = 1, @message = 'Bueno, hasta luego', @assistantName = 'Analia';