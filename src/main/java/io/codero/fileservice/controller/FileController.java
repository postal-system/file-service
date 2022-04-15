package io.codero.fileservice.controller;

import io.codero.fileservice.entity.FileEntity;
import io.codero.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    // TODO: 15.04.2022 обработать исключения
    private final FileService service;

    @PostMapping
    public UUID submit(@RequestBody MultipartFile file) throws IOException {
        return service.save(file);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<Resource> download(@PathVariable UUID id) throws IOException {
        FileEntity file = service.findById(id);
        byte[] data = file.getContent();
        String filename = file.getFileName();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", filename));
        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(data));
    }

    @DeleteMapping(value = "{id}")
    void delete(@PathVariable UUID id) throws IOException {
        service.delete(id);
    }
}
