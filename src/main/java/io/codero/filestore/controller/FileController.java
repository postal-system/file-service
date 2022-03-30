package io.codero.filestore.controller;

import io.codero.filestore.facade.FileStoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileStoreFacade service;

    @PostMapping
    public UUID submit(@RequestBody final MultipartFile file) {
        return service.saveFile(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") UUID id, @RequestBody final MultipartFile file) {
        service.update(file, id);
    }
}
