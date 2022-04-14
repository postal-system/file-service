package io.codero.filestore.service.impl;

import io.codero.filestore.exception.CastIOException;
import io.codero.filestore.exception.FileAlreadyExistException;
import io.codero.filestore.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${app.document-root}")
    private String fileDir;

    @Override
    public void add(MultipartFile multipartFile) {
        isFileExist(multipartFile.getOriginalFilename());
        String localName = fileDir + "/" + multipartFile.getOriginalFilename();
        try {
            Files.createDirectories(Paths.get(fileDir));
            try (FileOutputStream fos = new FileOutputStream(localName)) {
                fos.write(multipartFile.getBytes());
                log.info("File: {} was to save to disk", localName);
            }
        } catch (IOException ioException) {
            generateMessage(ioException);
        }
    }

    @Override
    public Resource getByFileName(String fileName) {
        try {
            return new InputStreamResource(new FileInputStream(fileDir + "/" + fileName));
        } catch (IOException ioException) {
            generateMessage(ioException);
        }
        return null;
    }

    @Override
    public void update(MultipartFile file, String oldName) {
        String newName = file.getOriginalFilename();
        try {
            Files.deleteIfExists(Path.of(fileDir, oldName));
            log.info("Old File: {} was deleted", oldName);
            String localName = fileDir + "/" + newName;
            try (FileOutputStream fos = new FileOutputStream(localName)) {
                fos.write(file.getBytes());
                log.info("File: {} was replaced to new file", localName);
            }
        } catch (IOException ioException) {
            generateMessage(ioException);
        }
    }

    @Override
    public void deleteByFileName(String fileName) {
        try {
            Files.deleteIfExists(Path.of(fileDir, fileName));
        } catch (IOException ioException) {
            generateMessage(ioException);
        }
        log.info("File: {} was deleted from disk", fileName);
    }

    private void isFileExist(String fileName) {
        if (Files.exists(Path.of(fileDir, fileName))) {
            String message = String.format("The file with name: \"%s\" already exists", fileName);
            log.error("{}", message);
            throw new FileAlreadyExistException(message);
        }
    }

    private void generateMessage(IOException ioException) {
        log.error("It was throw IOException, File cannot be deleted {}", ioException);
        throw new CastIOException("It was throw IOException, file cannot be deleted");
    }
}
