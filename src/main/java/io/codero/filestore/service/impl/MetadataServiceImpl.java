package io.codero.filestore.service.impl;

import io.codero.filestore.entity.Metadata;
import io.codero.filestore.exception.FileNotFoundException;
import io.codero.filestore.repository.MetadataRepository;
import io.codero.filestore.service.MetadataService;
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
                .orElseThrow(() -> new FileNotFoundException(""))
                .getFileName();
    }

    @Override
    public void update(Metadata metaData) {

    }

    @Override
    public void deleteByID(UUID uuid) {

    }
}
