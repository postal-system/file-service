package io.codero.filestore.repository;

import io.codero.filestore.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface MetadataRepository extends JpaRepository<Metadata, UUID> {
}
