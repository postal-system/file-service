package io.codero.filestore.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void add(MultipartFile multipartFile);

    Resource getByFileName(String fileName);

    void update(MultipartFile multipartFile, String oldName);

    void deleteByFileName(String fileName);
}
