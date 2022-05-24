USE das_rescue_app;
GO;

CREATE OR ALTER TRIGGER UserCancelationTrigger
    ON dbo.assistance
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE
        @user_id INT, @lowcount INT, @highcount INT
    SELECT @user_id = INSERTED.user_id
    FROM INSERTED
    SELECT @lowcount = COUNT(*)
    FROM dbo.assistance a
             JOIN assistance_status as2
                  ON as2.id = a.assistance_status_id
             JOIN INSERTED i
                  ON i.assistance_status_id = a.assistance_status_id
    WHERE a.timestamp > (GETDATE() - 30)
      AND as2.name = N'canceled'
      AND a.user_id = @user_id
    SELECT @highcount = COUNT(*)
    FROM dbo.assistance a
             JOIN assistance_status as2
                  ON as2.id = a.assistance_status_id
             JOIN INSERTED i
                  ON i.assistance_status_id = a.assistance_status_id
    WHERE a.timestamp > (GETDATE() - 180)
      AND as2.name = N'canceled'
      AND a.user_id = @user_id
    IF @lowcount >= 3 OR @highcount >= 10
        BEGIN
            UPDATE dbo.[user]
            SET canceled = 1
            WHERE [user].id = @user_id
        END
END;
GO;