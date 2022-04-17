--liquibase formatted sql
--changeset asmirnov@codero.io:1
--comment: create table

CREATE TABLE if NOT EXISTS file(
    id UUID PRIMARY KEY,
    file_name VARCHAR(500)
);