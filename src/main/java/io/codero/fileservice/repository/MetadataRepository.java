package io.codero.fileservice.repository;

import io.codero.fileservice.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetadataRepository extends JpaRepository<Metadata, UUID> {
}
