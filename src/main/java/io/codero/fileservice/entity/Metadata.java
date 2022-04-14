package io.codero.fileservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "file")
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "file_name", nullable = false)
    private String fileName;
}
