package com.labhesh.Todos.Todos.app.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilesRepo extends JpaRepository<Files, UUID> {
    Optional<Files> findByFileName(String fileName);
    Optional<Files> findByFileType(String fileType);
}
