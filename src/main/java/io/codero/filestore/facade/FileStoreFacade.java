package io.codero.filestore.facade;

import io.codero.filestore.entity.Metadata;
import io.codero.filestore.service.FileService;
import io.codero.filestore.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStoreFacade {
    private final FileService fileService;
    private final MetadataService metadataService;

    public UUID saveFile(MultipartFile multipartFile) {
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

    public void update(MultipartFile file, UUID uuid) {
        String oldName = metadataService.getNameById(uuid);
        fileService.update(file, oldName);
        metadataService.deleteByID(uuid);
        metadataService.create(new Metadata(uuid, file.getOriginalFilename()));
    }
}