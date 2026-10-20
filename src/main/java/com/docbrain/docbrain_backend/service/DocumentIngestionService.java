package com.docbrain.docbrain_backend.service;

import com.docbrain.docbrain_backend.entity.DocumentMetadata;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentIngestionService {
    public int ingest(DocumentMetadata documentMetadata, List<Document> parsedDocs) {
        return 0;
    }
}
