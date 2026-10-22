package com.docbrain.docbrain_backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponseDto {

    private String answer;
    private String conversationId;
    private List<CitationDto> citations;
    private Long responseTimeMs;

}
