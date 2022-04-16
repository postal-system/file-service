package io.codero.fileservice.service;

import io.codero.fileservice.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileService {
    UUID save(MultipartFile file) throws IOException;

    FileEntity findById(UUID uuid) throws IOException;

    void delete(UUID uuid) throws IOException;
}
