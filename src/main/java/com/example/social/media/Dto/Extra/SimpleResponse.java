package com.example.social.media.Dto.Extra;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record SimpleResponse(
        HttpStatus status,
        String message){
}