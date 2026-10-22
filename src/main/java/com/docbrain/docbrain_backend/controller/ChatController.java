package com.docbrain.docbrain_backend.controller;

import com.docbrain.docbrain_backend.dto.*;
import com.docbrain.docbrain_backend.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/chat")

@Tag(
        name = "Chat Manager",
        description = "Endpoints for all chats related apis"
)

@RequiredArgsConstructor
public class ChatController {

   private final RagService ragService;

   @PostMapping("/query")
   @Operation(summary = "Ask a question against all documents or a specific document with citatoin")
   public  ResponseEntity<ApiResponse<ChatResponseDto>> askQuestion(@Valid @RequestBody ChatRequestDto requestDto){

       ChatResponseDto chatResponseDto =  ragService.askQuestion(requestDto);
       return ResponseEntity.ok(
               ApiResponse.<ChatResponseDto>builder()
                       .success(true)
                       .message(null)
                       .data(chatResponseDto)
                       .timeStamp(LocalDateTime.now())
                       .build()
       );
   }

   @PostMapping("/stream")
   @Operation(summary = "Stream real0time Q&A answer tokens via Server-Sent Events (SSE)")
   public Flux<String> streamQuestion(@Valid @RequestBody ChatRequestDto requestDto){
       return ragService.streamQuestionAnswer(requestDto);
   }

    @PostMapping("/search/similarity")
    @Operation(summary = "Perform semantic similarity search on stored document vectors")
    public ResponseEntity<ApiResponse<SearchResultDto>> searchSimilar(@Valid @RequestBody SearchRequestDto request){
        SearchResultDto results = ragService.searchSimilarChunks(request);
        return ResponseEntity.ok(
                ApiResponse.
                <SearchResultDto>builder()
                .success(true)
                .message(null)
                .data(results)
                .timeStamp(LocalDateTime.now())
                .build()
        );
    }
}
