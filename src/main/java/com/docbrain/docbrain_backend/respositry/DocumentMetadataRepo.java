package com.docbrain.docbrain_backend.respositry;

import com.docbrain.docbrain_backend.dto.DocumentResponseDto;
import com.docbrain.docbrain_backend.entity.DocumentMetadata;
import com.docbrain.docbrain_backend.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentMetadataRepo extends JpaRepository<DocumentMetadata, UUID> {

    List<DocumentMetadata> findByStatus(DocumentStatus status);
    List<DocumentMetadata> findAllByOrderByCreatedAtDesc();
}
