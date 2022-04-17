--liquibase formatted sql
--changeset asmirnov@codero.io:2
--comment: added column 'context' for storage data from file

ALTER TABLE file
ADD if not exists content bytea NULL;