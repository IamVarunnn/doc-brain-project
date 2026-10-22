package com.docbrain.docbrain_backend.dto;

import com.docbrain.docbrain_backend.entity.DocumentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentMetadataDto {


    private UUID id;
    private String filename;
    private String contentType;
    private Long fileSize;
    private Integer totalPages;
    private Integer totalChunks;
    private DocumentStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
