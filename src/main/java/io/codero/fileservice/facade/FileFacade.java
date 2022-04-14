package io.codero.fileservice.facade;

import io.codero.fileservice.entity.Metadata;
import io.codero.fileservice.service.FileService;
import io.codero.fileservice.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileFacade {
    private final FileService fileService;
    private final MetadataService metadataService;

    public synchronized UUID save(MultipartFile multipartFile) {
        fileService.add(multipartFile);
        return metadataService.create(new Metadata(UUID.randomUUID(), multipartFile.getOriginalFilename()));
    }

    public Resource getById(UUID uuid) {
        String fileName = metadataService.getNameById(uuid);
        return fileService.getByFileName(fileName);
    }

    public void deleteById(UUID uuid) {
        String fileName = metadataService.getNameById(uuid);
        fileService.deleteByFileName(fileName);
        metadataService.deleteByID(uuid);
    }

    public synchronized void update(MultipartFile file, UUID uuid) {
        String oldName = metadataService.getNameById(uuid);
        fileService.update(file, oldName);
        metadataService.deleteByID(uuid);
        metadataService.create(new Metadata(uuid, file.getOriginalFilename()));
    }
}