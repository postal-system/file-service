package io.codero.fileservice.service.impl;

import io.codero.fileservice.entity.FileEntity;

import io.codero.fileservice.exception.NotFoundException;
import io.codero.fileservice.repository.FileRepository;
import io.codero.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                .orElseThrow(() -> new NotFoundException(String.format("File wit id = %s cannot be found", uuid)));
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public void delete(UUID uuid) throws NotFoundException {
        findById(uuid);
        fileRepository.deleteById(uuid);
    }
}
