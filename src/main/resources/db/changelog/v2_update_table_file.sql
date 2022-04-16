ALTER TABLE file
ADD if not exists creation_timestamp timestamp,
ADD if not exists update_timestamp timestamp;