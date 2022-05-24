USE das_entity;

INSERT INTO assistance_status(name)
VALUES ('in_progress'),
       ('finalized'),
       ('canceled');

INSERT INTO message_type(name)
VALUES ('creation'),
       ('message'),
       ('finalization'),
       ('cancelation'),
       ('valoration');

INSERT INTO reason(name)
VALUES ('poorly_attended'),
       ('issue_already_solved'),
       ('too_much_waiting');

USE das_firedept;

INSERT INTO assistance_status(name)
VALUES ('in_progress'),
       ('finalized'),
       ('canceled');

INSERT INTO message_type(name)
VALUES ('creation'),
       ('message'),
       ('finalization'),
       ('cancelation'),
       ('valoration');

INSERT INTO reason(name)
VALUES ('poorly_attended'),
       ('issue_already_solved'),
       ('too_much_waiting');

USE das_police;

INSERT INTO assistance_status(name)
VALUES ('in_progress'),
       ('finalized'),
       ('canceled');

INSERT INTO message_type(name)
VALUES ('creation'),
       ('message'),
       ('finalization'),
       ('cancelation'),
       ('valoration');

INSERT INTO reason(name)
VALUES ('issue_already_solved'),
       ('too_much_waiting');

USE das_ambulance;

INSERT INTO assistance_status(name)
VALUES ('in_progress'),
       ('finalized'),
       ('canceled');

INSERT INTO message_type(name)
VALUES ('creation'),
       ('message'),
       ('finalization'),
       ('cancelation'),
       ('valoration');

INSERT INTO reason(name)
VALUES ('too_much_waiting');

USE das_civildef;

INSERT INTO assistance_status(name)
VALUES ('in_progress'),
       ('finalized'),
       ('canceled');

INSERT INTO message_type(name)
VALUES ('creation'),
       ('message'),
       ('finalization'),
       ('cancelation'),
       ('valoration');

INSERT INTO reason(name)
VALUES ('poorly_attended'),
       ('too_much_waiting');

USE das_muni;

INSERT INTO assistance_status(name)
VALUES ('in_progress'),
       ('finalized'),
       ('canceled');

INSERT INTO message_type(name)
VALUES ('creation'),
       ('message'),
       ('finalization'),
       ('cancelation'),
       ('valoration');

INSERT INTO reason(name)
VALUES ('poorly_attended'),
       ('issue_already_solved');
