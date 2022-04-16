package io.codero.fileservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "file")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;

    @Column(name = "creation_timestamp", nullable = false)
    private LocalDateTime creationTimestamp;

    @Column(name = "update_timestamp", nullable = false)
    private LocalDateTime updateTimestamp;
}
