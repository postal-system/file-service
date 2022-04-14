package io.codero.fileservice.service;

import io.codero.fileservice.entity.Metadata;

import java.util.UUID;

public interface MetadataService {
    UUID create(Metadata metadata);

    String getNameById(UUID uuid);

    void update(Metadata metaData);

    void deleteByID(UUID uuid);
}
