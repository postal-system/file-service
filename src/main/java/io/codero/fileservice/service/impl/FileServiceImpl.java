package io.codero.fileservice.service.impl;

import io.codero.fileservice.entity.FileEntity;
import io.codero.fileservice.exception.ExceptionMessage;
import io.codero.fileservice.exception.FileNotFoundException;
import io.codero.fileservice.repository.FileRepository;
import io.codero.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    public synchronized UUID save(MultipartFile multipartFile) throws IOException {
        FileEntity file = FileEntity.builder()
                .fileName(multipartFile.getOriginalFilename())
                .content(multipartFile.getBytes())
                .creationTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();
        return fileRepository.save(file).getId();
    }

    @Override
    public FileEntity findById(UUID uuid) {
        return fileRepository.findById(uuid)
                .orElseThrow(() -> new FileNotFoundException(ExceptionMessage.FILE_NOT_FOUND_EXCEPTION.get()));
    }

    @Override
    public void delete(UUID uuid) {
        fileRepository.deleteById(uuid);
    }
}
