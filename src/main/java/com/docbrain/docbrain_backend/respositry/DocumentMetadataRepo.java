package com.docbrain.docbrain_backend.respositry;

import com.docbrain.docbrain_backend.entity.DocumentMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentMetadataRepo extends JpaRepository<DocumentMetadata, UUID> {
}
