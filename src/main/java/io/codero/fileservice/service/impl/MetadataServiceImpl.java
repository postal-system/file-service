package io.codero.fileservice.service.impl;

import io.codero.fileservice.entity.Metadata;
import io.codero.fileservice.exception.ExceptionMessage;
import io.codero.fileservice.exception.FileNotFoundException;
import io.codero.fileservice.repository.MetadataRepository;
import io.codero.fileservice.service.MetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {
    private final MetadataRepository repository;

    @Override
    public UUID create(Metadata metadata) {
        return repository.save(metadata).getId();
    }

    @Override
    public String getNameById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new FileNotFoundException(ExceptionMessage.FILE_NOT_FOUND_EXCEPTION.get()))
                .getFileName();
    }

    @Override
    public void update(Metadata metaData) {
        // TODO: make implementation
    }

    @Override
    public void deleteByID(UUID uuid) {
        // TODO: make implementation
    }
}
