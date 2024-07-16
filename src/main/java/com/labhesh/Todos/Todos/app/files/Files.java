package com.labhesh.Todos.Todos.app.files;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="files")
@Data
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "id", updatable = false)
    private UUID id;

    private String fileName;

    private String fileType;
    @JsonIgnore
    private byte[] data;
}
