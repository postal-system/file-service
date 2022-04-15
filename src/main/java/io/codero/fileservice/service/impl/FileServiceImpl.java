package io.codero.fileservice.service.impl;

import io.codero.fileservice.entity.FileEntity;
import io.codero.fileservice.exception.CustomIOException;
import io.codero.fileservice.exception.ExceptionMessage;
import io.codero.fileservice.exception.FileAlreadyExistException;
import io.codero.fileservice.repository.FileRepository;
import io.codero.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    public UUID save(MultipartFile multipartFile) throws IOException {
        FileEntity file = new FileEntity();

        file.setFileName(multipartFile.getOriginalFilename());
        file.setContent(multipartFile.getBytes());

        return fileRepository.save(file).getId();
    }
}
