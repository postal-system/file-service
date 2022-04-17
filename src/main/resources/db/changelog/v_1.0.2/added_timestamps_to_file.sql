--liquibase formatted sql
--changeset asmirnov@codero.io:3
--comment: added columns 'creation_timestamp' and 'update_timestamp' for file

ALTER TABLE file
ADD if not exists creation_timestamp timestamp,
ADD if not exists update_timestamp timestamp;